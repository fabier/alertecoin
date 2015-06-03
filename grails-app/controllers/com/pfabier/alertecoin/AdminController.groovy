package com.pfabier.alertecoin

import grails.plugin.springsecurity.annotation.Secured

@Secured("hasRole('ADMIN')")
class AdminController {

    def index() {
        render view: "index"
    }
}
