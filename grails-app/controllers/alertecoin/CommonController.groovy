package alertecoin

import grails.plugin.mail.MailService
import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.SpringSecurityUtils
import org.springframework.security.access.annotation.Secured

@Secured(['permitAll'])
class CommonController {

    MailService mailService
    SpringSecurityService springSecurityService

    def index() {
        User user = springSecurityService.currentUser
        if (user == null) {
            String postUrl = "${request.contextPath}${SpringSecurityUtils.securityConfig.apf.filterProcessesUrl}"
            render view: "/common/index", model: [postUrl: postUrl]
        } else {
            redirect controller: "alert", action: "index"
        }
    }

    def about() {
        String postUrl = "${request.contextPath}${SpringSecurityUtils.securityConfig.apf.filterProcessesUrl}"
        render view: "/common/about", model: [postUrl: postUrl]
    }

    def contact() {
        String postUrl = "${request.contextPath}${SpringSecurityUtils.securityConfig.apf.filterProcessesUrl}"
        render view: "/common/contact", model: [postUrl: postUrl]
    }

    def sendMail(SendMailCommand sendMailCommand) {
        if (sendMailCommand.hasErrors()) {
            flash.warning = "Merci de renseigner votre adresse email et votre message"
            redirect action: "contact"
        } else {
            String messageWithBr = sendMailCommand.message.replaceAll("\\n", "<br/>")
            String message = "<html><head></head><body>${messageWithBr}</body></html>"
            String messageAck = "<html><head></head><body>Bonjour,<br/><br/>Vous venez d'envoyer le message suivant à AlerteCoin.<br/>Nous nous efforçons de répondre à votre demande dans les plus brefs délais.<br/><br/>Cordialement,<br/><br/>L'équipe AlerteCoin.<br/><br/>#######################<br/><br/>${messageWithBr}</body></html>"
            mailService.sendMail {
                async true
                to "AlerteCoin <${grailsApplication.config.grails.mail.username}>"
                subject "AlerteCoin - Nouveau message depuis le site web"
                html message
                from sendMailCommand.email
            }
            mailService.sendMail {
                async true
                to sendMailCommand.email
                subject "AlerteCoin - Confirmation d'envoi d'un message"
                html messageAck
                from "AlerteCoin <${grailsApplication.config.grails.mail.username}>"
            }
            flash.message = "Votre message a été envoyé, il sera traité dans les plus brefs délais."
            redirect action: "contact"
        }
    }
}

