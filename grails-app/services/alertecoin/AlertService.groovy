package alertecoin

import grails.gsp.PageRenderer
import grails.plugin.mail.MailService
import grails.util.Environment
import org.apache.commons.lang.StringUtils
import org.apache.commons.lang.time.DateUtils
import org.apache.http.NameValuePair
import org.apache.http.client.utils.URIBuilder

class AlertService {

    LeBonCoinParserService leBonCoinParserService
    PageRenderer groovyPageRenderer
    def grailsApplication
    MailService mailService
    ClassifiedService classifiedService

    def create(String name, String url, Integer checkIntervalInMinutes, User user) {
        Alert alert = new Alert(
                name: name,
                url: url,
                checkIntervalInMinutes: checkIntervalInMinutes,
                user: user,
                creator: user,
                state: State.ACTIVE
        )
        alert.nextCheckDate = calculateNextCheckDate(alert)
        fillWithCurrentClassifiedsOnPage(alert)
        return alert.save(flush: true)
    }

    def update(Alert alert, String name, String url, Integer checkIntervalInMinutes) {
        if (alert != null) {
            alert.name = name
            alert.checkIntervalInMinutes = checkIntervalInMinutes

            url = normalizeURL(url);

            if (!StringUtils.equalsIgnoreCase(url, alert.url)) {
                // Modification d'url, on reset cette recherche
                alert.url = url
                alert.lastCheckedDate = null
                alert.mostRecentClassifiedDate = null
                clearClassifieds(alert)

                // On recherche les annonces pour cette alerte
                fillWithCurrentClassifiedsOnPage(alert)
                alert.nextCheckDate = calculateNextCheckDate(alert)
            }
            alert.save(flush: true, failOnError: true)
        }
        return alert
    }

    /**
     * Cette méthode prend en entrée une URL et la retravaille pour qu'elle fonctionne parfaitement avec le système.
     * Par exemple, elle change la valeur de query param "sp" à 0 si elle est positionnée, pour éviter de ne pas avoir les toutes deernières offres à jour.
     * @param url
     * @return
     */
    private static String normalizeURL(String url) {
        URIBuilder builder = new URIBuilder(url)
        List<NameValuePair> nameValuePairs = builder.getQueryParams()
        builder.removeQuery()
        for (NameValuePair nameValuePair : nameValuePairs) {
            String paramName = nameValuePair.getName()
            if ("sp".equals(paramName)) {
                builder.addParameter(paramName, "0")
            } else {
                builder.addParameter(paramName, nameValuePair.getValue())
            }
        }
        return builder.toString()
    }

    def fillWithCurrentClassifiedsOnPage(Alert alert) {
        List<Classified> classifieds = leBonCoinParserService.getClassifieds(alert.url)
        if (classifieds && !classifieds?.isEmpty()) {
            // On supprime les anciennes annonces
            alert.classifieds?.toList()?.each {
                alert.removeFromClassifieds(it)
                it.delete()
            }
            classifieds.sort { a, b ->
                -a.date.compareTo(b.date)
            }
            classifieds.each {
                alert.addToClassifieds(it)
            }
        }
        alert.lastCheckedDate = new Date() // Vérifié pour la dernière fois maintenant
        if (!classifieds.isEmpty()) {
            // La liste des classifieds contient un élément, on prend sa date comme référence
            alert.mostRecentClassifiedDate = classifieds.first()?.date
        } else {
            // La liste des classifieds est vide, tant pis la mostRecentClassifiedDate ne change pas de valeur
        }
        alert.save(flush: true)
    }

    def fillWithNewClassifiedsAndSendEmailIfNewFound(Alert alert) {
        try {
            // Même si la recherche plante, on marque qu'on a essayé de mettre à jour
            // Vérifié pour la dernière fois maintenant, on met à jour la date de dernière vérification
            alert.lastCheckedDate = new Date()
            if (alert.checkIntervalInMinutes == null) {
                alert.checkIntervalInMinutes = 5
            }
            alert.nextCheckDate = calculateNextCheckDate(alert)

            List<Classified> classifieds = leBonCoinParserService.getClassifieds(alert.url, alert.mostRecentClassifiedDate)

            User user = alert.user

            if (classifieds && !classifieds.isEmpty()) {
                log.info "Found ${classifieds.size() ?: 0} classifieds for alert : [${alert.id}] ${alert.name}"

                clearClassifieds(alert)
                // On trie pour remonter les annonces les plus récentes en premier
                classifieds.sort { a, b ->
                    -a.date.compareTo(b.date)
                }
                classifieds.each {
                    alert.addToClassifieds(it)
                }

                // Date de dernière annonce déposée et scannée
                if (!classifieds.isEmpty()) {
                    // La liste des classifieds contient un élément, on prend sa date comme référence
                    alert.mostRecentClassifiedDate = classifieds.first()?.date
                } else {
                    // La liste des classifieds est vide, tant pis la mostRecentClassifiedDate ne change pas de valeur
                }

                log.info "Sending email with ${classifieds.size()} new classifieds to ${user.email} : [${alert.id}] ${alert.name} (${user.displayName ?: user.email})..."

                // ... et on envoie un mail
                sendEmailWithNewClassified(alert, classifieds)
            } else {
                log.info "No new classifieds : [${alert.id}] ${alert.name} (${user.displayName ?: user.email})"
                // On vérifie tout de même que les annonces sont à jour...
                def classifiedsCopied = alert.classifieds?.toList()
                for (Classified classified : classifiedsCopied) {
                    if (!classifiedService.isClassifiedStillOnline(classified)) {
                        alert.removeFromClassifieds(classified)
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error in AlertService : fillWithNewClassifiedsAndSendEmailIfNewFound", e)
            // Quelle que soit l'erreur, on reprogramme cette alerte pour dans 5 minutes
            Calendar calendar = new GregorianCalendar()
            calendar.add(Calendar.MINUTE, 5)
            alert.nextCheckDate = calendar.getTime()
        } finally {
            // Sauvegarde de l'état en base
            alert.save(flush: true)
        }
    }

    def scanForNewClassifieds(Alert alert) {
        return leBonCoinParserService.getClassifieds(alert.url, alert.mostRecentClassifiedDate)
    }

    def sendEmailWithNewClassified(Alert alert, List<Classified> classifieds) {
        User user = alert.user

        String body = groovyPageRenderer.render(view: "/email/email", model: [alert: alert, classifieds: classifieds, user: user])

        if (Environment.current != Environment.DEVELOPMENT) {
            mailService.sendMail {
                async true
                to user.email
                subject "AlerteCoin - ${alert.name}"
                html body
                from "AlerteCoin <${grailsApplication.config.grails.mail.username}>"
            }
            log.info "Email sent to ${user.displayName ?: user.email}, containing ${classifieds.size()} new classifieds."
        } else {
            // Uncomment to see email content in logs
            // log.info body
            log.info "Email NOT sent to ${user.displayName ?: user.email}, BECAUSE IN DEVELOPMENT MODE (${classifieds.size()} new classifieds)."
        }
    }

    def clearClassifieds(Alert alert) {
        // On supprime les anciennes annonces
        alert.classifieds?.toList()?.each {
            alert.removeFromClassifieds(it)
        }
    }

    def calculateNextCheckDate(Alert alert) {
        Calendar calendar = new GregorianCalendar()
        int checkIntervalInMinutes = alert?.checkIntervalInMinutes ?: 60
        calendar.add(Calendar.MINUTE, checkIntervalInMinutes)
        if (checkIntervalInMinutes >= 1440) {
            // Tous les jours à 8h du matin
            DateUtils.truncate(calendar, Calendar.HOUR)
            calendar.add(Calendar.HOUR, 8) // 8h du matin
        } else if (checkIntervalInMinutes >= 60) {
            // Toutes les heures (à l'heure pile)
            DateUtils.truncate(calendar, Calendar.MINUTE)
        } else {
            // Dès que possible, mais à la minute près
            DateUtils.truncate(calendar, Calendar.SECOND)
        }
        return calendar.getTime()
    }
}
