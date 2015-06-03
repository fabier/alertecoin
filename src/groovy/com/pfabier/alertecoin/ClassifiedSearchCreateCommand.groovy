package com.pfabier.alertecoin

import grails.validation.Validateable

@Validateable
class ClassifiedSearchCreateCommand {
    String name
    String url
    Integer checkIntervalInMinutes

    static constraints = {
        name blank: false
        url blank: false, validator: {
            it.startsWith("http://www.leboncoin.fr/")
        }
        checkIntervalInMinutes nullable: true, validator: { val, obj -> val in [5, 10, 15, 30, 60, 120, 240, 1440] }
    }
}