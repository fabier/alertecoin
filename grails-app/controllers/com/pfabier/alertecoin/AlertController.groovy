package com.pfabier.alertecoin

import grails.plugin.mail.MailService
import grails.plugin.springsecurity.SpringSecurityService
import org.springframework.security.access.annotation.Secured

import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.ReentrantLock

@Secured("hasRole('ROLE_USER')")
class AlertController {

    SpringSecurityService springSecurityService
    MailService mailService
    AlertService alertService

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
        if (alert == null || !alert.user.equals(user)) {
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

    def create(ClassifiedSearchCreateCommand command) {
        User user = springSecurityService.currentUser
        if (user.alerts?.size() >= 10) {
            // On met quand même une limite sinon on n'a pas fini !
            flash.message = "Impossible de créer une nouvelle alerte, vous avez déjà créé 10 alertes.<br/>Veuillez supprimer une alerte pour créer cette nouvelle alerte"
            flash.level = "error"
            redirect controller: "alert", action: "index"
        } else {
            if (command) {
                if (command.hasErrors()) {
                    render view: "create", model: [command: command]
                } else {
                    // On essaie de créer l'alerte
                    Alert alert = alertService.createOrUpdate(null, command.name, command.url, user)
                    log.info "command.checkIntervalInMinutes = ${command.checkIntervalInMinutes}"
                    alert.checkIntervalInMinutes = command.checkIntervalInMinutes
                    alert.nextCheckDate = alertService.calculateNextCheckDate(alert)

                    alertService.fillWithCurrentClassifiedsOnPage(alert)

                    redirect controller: "alert", action: "show", id: alert.id
                }
            } else {
                command = new ClassifiedSearchCreateCommand(name: "", url: "")
                render view: 'create', model: [command: command]
            }
        }
    }

    def update(ClassifiedSearchUpdateCommand command) {
        User user = springSecurityService.currentUser
        if (command) {
            if (command.hasErrors()) {
                render view: "show", id: command.id
            } else {
                Alert alert = Alert.get(command.id)
                if (command.url?.equals(alert.url)) {
                    // Pas de modification de l'url, on met juste à jour le titre
                    alert.name = command.name
                    alert.checkIntervalInMinutes = command.checkIntervalInMinutes
                } else {
                    // Modification d'url, on reset cette recherche
                    alert.lastCheckedDate = null
                    alert.mostRecentClassifiedDate = null
                    alertService.clearClassifieds(alert)
                    alert = alertService.createOrUpdate(command.id, command.name, command.url, user)
                    alertService.fillWithCurrentClassifiedsOnPage(alert)
                    log.info "command.checkIntervalInMinutes = ${command.checkIntervalInMinutes}"
                    alert.checkIntervalInMinutes = command.checkIntervalInMinutes
                    alert.nextCheckDate = alertService.calculateNextCheckDate(alert)
                }
                alert.save()

                redirect controller: "alert", action: "show", id: alert.id
            }
        } else {
            redirect action: "index"
        }
    }

    def delete(long id) {
        Alert alert = Alert.get(id)
        User user = springSecurityService.currentUser
        if (alert == null || !alert.user.equals(user)) {
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
        User user = springSecurityService.currentUser
        if (alert == null || !alert.user.equals(user)) {
            response.sendError(404)
        } else {
            alertService.fillWithNewClassifiedsAndSendEmailIfNewFound(alert)
            redirect action: "show", id: id
        }
    }

    def email(long id) {
        Alert alert = Alert.get(id)
        User user = springSecurityService.currentUser
        if (alert == null || !alert.user.equals(user)) {
            response.sendError(404)
        } else {
            def classifieds = alert.classifieds?.reverse()
            render view: "/email/email", model: [alert: alert, classifieds: classifieds, user: user]
        }
    }

    def edit(long id) {
        Alert alert = Alert.get(id)
        User user = springSecurityService.currentUser
        if (alert == null || !alert.user.equals(user)) {
            response.sendError(404)
        } else {
            render view: "edit", model: [alert: alert]
        }
    }

    def confirmDelete(long id) {
        Alert alert = Alert.get(id)
        User user = springSecurityService.currentUser
        if (alert == null || !alert.user.equals(user)) {
            response.sendError(404)
        } else {
            render view: "confirmDelete", model: [alert: alert]
        }
    }
}

