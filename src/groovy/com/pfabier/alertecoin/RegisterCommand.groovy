package com.pfabier.alertecoin

import grails.plugin.springsecurity.SpringSecurityUtils
import grails.validation.Validateable

@Validateable
class RegisterCommand {

    String username
    String email
    String password
    String password2

    def grailsApplication

    static constraints = {
        username blank: false, validator: { value, command ->
            if (value) {
                def User = command.grailsApplication.getDomainClass(
                        SpringSecurityUtils.securityConfig.userLookup.userDomainClassName).clazz
                if (User.findByUsername(value)) {
                    return 'registerCommand.username.unique'
                }
            }
        }
        email blank: false, email: true
        password blank: false, validator: RegisterController.passwordValidator
        password2 validator: RegisterController.password2Validator
    }
}
