package com.pfabier.alertecoin

import grails.gsp.PageRenderer
import grails.plugin.mail.MailService
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class AlertService {

    LeBonCoinParserService leBonCoinParserService
    PageRenderer groovyPageRenderer
    def grailsApplication
    MailService mailService

    def createOrUpdate(Long id, String name, String url, User user) {
        Alert alert = null
        if (id != null) {
            alert = Alert.get(id)
            if (alert != null) {
                alert.name = name
                alert.url = url
            }
        } else {
            alert = new Alert(name: name, url: url, user: user)
            alert.save(failOnError: true)
        }
        return alert
    }

    def fillWithCurrentClassifiedsOnPage(Alert alert) {
        Document doc = Jsoup.connect(alert.url).get();
        List<Classified> classifieds = leBonCoinParserService.getClassifieds(doc)
        if (!classifieds?.isEmpty()) {
            // On supprime les anciennes annonces
            alert.classifieds?.toList()?.each {
                alert.removeFromClassifieds(it)
                it.delete()
            }
            classifieds.sort { a, b ->
                -a.date.compareTo(b.date)
            }
            classifieds.each {
                it.save(failOnError: true)
                alert.addToClassifieds(it)
            }
            alert.lastCheckedDate = new Date() // Vérifié pour la dernière fois maintenant
            alert.mostRecentClassifiedDate = classifieds.first().date // Date de dernière annonce déposée
            alert.save(failOnError: true)
        }
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
            log.info "nextCheckDate = ${alert.nextCheckDate.format("HH:mm")}"
            alert.save()

            Document doc = Jsoup.connect(alert.url).get();
            List<Classified> classifieds = leBonCoinParserService.getClassifieds(doc, alert.mostRecentClassifiedDate)

            if (classifieds && !classifieds.isEmpty()) {
                log.info "Found ${classifieds.size() ?: 0} classifieds for alert : [${alert.id}] ${alert.name}"

                clearClassifieds(alert)
                // On trie pour remonter les annonces les plus récentes en premier
                classifieds.sort { a, b ->
                    -a.date.compareTo(b.date)
                }
                classifieds.each {
                    it.save()
                    alert.addToClassifieds(it)
                }
                // Date de dernière annonce déposée et scannée
                alert.mostRecentClassifiedDate = classifieds.first().date

                User user = alert.user
                log.info "Trying to send ${classifieds.size()} new classifieds to ${user.displayName ?: user.email} for alert : [${alert.id}] ${alert.name}"

                // ... et on envoie un mail
                sendEmailWithNewClassified(alert, classifieds)
            } else {
                log.info "No new classifieds for alert : [${alert.id}] ${alert.name}"
            }
        } catch (Exception e) {
            log.error("Error in AlertService : fillWithNewClassifiedsAndSendEmailIfNewFound", e)
            // Quelle que soit l'erreur, on reprogramme cette alerte pour dans 5 minutes
            Calendar calendar = new GregorianCalendar()
            calendar.add(Calendar.MINUTE, 5)
            alert.nextCheckDate = calendar.getTime()
            alert.save()
        } finally {
        }
    }

    def scanForNewClassifieds(Alert alert) {
        Document doc = Jsoup.connect(alert.url).get();
        return leBonCoinParserService.getClassifieds(doc, alert.mostRecentClassifiedDate)
    }

    def sendEmailWithNewClassified(Alert alert, List<Classified> classifieds) {
        User user = alert.user

        String body = groovyPageRenderer.render(view: "/email/email", model: [alert: alert, classifieds: classifieds, user: user])

        mailService.sendMail {
            async true
            to user.email
            subject "AlerteCoin - ${alert.name}"
            html body
            from "AlerteCoin <${grailsApplication.config.grails.mail.username}>"
        }

        log.info "Email sent to ${user.displayName ?: user.email}, containing ${classifieds.size()} new classifieds."
    }

    def clearClassifieds(Alert alert) {
        // On supprime les anciennes annonces
        alert.classifieds?.toList()?.each {
            alert.removeFromClassifieds(it)
            it.delete()
        }
    }

    def calculateNextCheckDate(Alert alert) {
        Calendar calendar = new GregorianCalendar()
        calendar.add(Calendar.MINUTE, alert?.checkIntervalInMinutes ?: 5)
        return calendar.getTime()
    }
}