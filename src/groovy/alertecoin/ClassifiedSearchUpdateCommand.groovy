package alertecoin

import grails.validation.Validateable

@Validateable
class ClassifiedSearchUpdateCommand {
    Long id
    String name
    String url
    Integer checkIntervalInMinutes

    static constraints = {
        id nullable: false
        name blank: false
        url blank: false, validator: {
            it.startsWith("https://www.leboncoin.fr/") || it.startsWith("http://www.leboncoin.fr/")
        }
        checkIntervalInMinutes nullable: true, validator: { val, obj -> val in [5, 10, 15, 30, 60, 120, 240, 1440] }
    }
}
