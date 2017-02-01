package alertecoin

import grails.plugin.mail.MailService
import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.SpringSecurityUtils
import org.springframework.security.access.annotation.Secured

@Secured("hasRole('ROLE_USER')")
class AlertController {

    SpringSecurityService springSecurityService
    MailService mailService
    AlertService alertService
    UserService userService
    AccessControlService accessControlService

    def index() {
        User user = springSecurityService.currentUser
        def alerts = user.alerts.sort { a, b ->
            a.name.compareToIgnoreCase(b.name)
        }
        render view: "index", model: [alerts: alerts]
    }

    def show(long id) {
        Alert alert = Alert.get(id)
        User user = springSecurityService.currentUser
        boolean hasRights = accessControlService.hasRights(user, alert)
        if (alert == null || !hasRights) {
            response.sendError(404)
        } else {
            def classifieds = alert.classifieds.sort { a, b ->
                -a.date.compareTo(b.date)
            }
            render view: "show", model: [
                    alert      : alert,
                    classifieds: classifieds
            ]
        }
    }

    def 'new'() {
        User user = springSecurityService.currentUser
        if (user.alerts?.size() >= 10) {
            // On met quand même une limite sinon on n'a pas fini !
            flash.error = "Impossible de créer une nouvelle alerte, vous avez déjà créé 10 alertes.<br/>Veuillez supprimer une alerte pour créer une nouvelle alerte"
            redirect action: "index"
        } else {
            def command = new ClassifiedSearchCreateCommand(name: "", url: "")
            render view: 'create', model: [command: command]
        }
    }

    def create(ClassifiedSearchCreateCommand command) {
        User user = springSecurityService.currentUser
        if (user.alerts?.size() >= 10) {
            // On met quand même une limite sinon on n'a pas fini !
            flash.error = "Impossible de créer une nouvelle alerte, vous avez déjà créé 10 alertes.<br/>Veuillez supprimer une alerte pour créer une nouvelle alerte"
            render view: "create", model: [command: command]
        } else {
            if (command.hasErrors()) {
                flash.error = "Merci de vérifier votre saisie"
                render view: "create", model: [command: command]
            } else {
                // On essaie de créer l'alerte
                Alert alert = alertService.create(command.name, command.url, command.checkIntervalInMinutes, user)
                redirect controller: "alert", action: "show", id: alert.id
            }
        }
    }

    def update(ClassifiedSearchUpdateCommand command) {
        if (command) {
            if (command.hasErrors()) {
                flash.error = "Merci de vérifier votre saisie"
                redirect action: "edit", id: command.id
            } else {
                Alert alert = Alert.get(command.id)
                alertService.update(alert, command.name, command.url, command.checkIntervalInMinutes)
                redirect controller: "alert", action: "show", id: alert.id
            }
        } else {
            redirect action: "index"
        }
    }

    def delete(long id) {
        Alert alert = Alert.get(id)
        User user = springSecurityService.currentUser
        boolean hasRights = accessControlService.hasRights(user, alert)
        if (alert == null || !hasRights) {
            response.sendError(404)
        } else {
            Alert.withTransaction {
                user.removeFromAlerts(alert)
                alert.classifieds?.toList().each {
                    alert.removeFromClassifieds(it)
                }
                alert.delete()
            }
            redirect controller: "alert", action: "index"
        }
    }

    def refresh(long id) {
        Alert alert = Alert.get(id)
        boolean hasRights = accessControlService.hasRights(user, alert)
        if (alert == null || !hasRights) {
            response.sendError(404)
        } else {
            alertService.fillWithNewClassifiedsAndSendEmailIfNewFound(alert)
            redirect action: "show", id: id
        }
    }

    def email(long id) {
        Alert alert = Alert.get(id)
        User user = springSecurityService.currentUser
        boolean hasRights = accessControlService.hasRights(user, alert)
        if (alert == null || !hasRights) {
            response.sendError(404)
        } else {
            def classifieds = alert.classifieds.sort { a, b ->
                -a.date.compareTo(b.date)
            }
            render view: "/email/email", model: [alert: alert, classifieds: classifieds, user: user]
        }
    }

    def edit(long id) {
        Alert alert = Alert.get(id)
        User user = springSecurityService.currentUser
        boolean hasRights = accessControlService.hasRights(user, alert)
        if (alert == null || !hasRights) {
            response.sendError(404)
        } else {
            render view: "edit", model: [alert: alert]
        }
    }

    def confirmDelete(long id) {
        Alert alert = Alert.get(id)
        User user = springSecurityService.currentUser
        boolean hasRights = accessControlService.hasRights(user, alert)
        if (alert == null || !hasRights) {
            response.sendError(404)
        } else {
            render view: "confirmDelete", model: [alert: alert]
        }
    }
}

