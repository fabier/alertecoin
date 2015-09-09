package alertecoin

import grails.validation.Validateable

@Validateable
class SendMailCommand {
    String email
    String message

    static constraints = {
        email email: true
        message nullable: false
    }
}
