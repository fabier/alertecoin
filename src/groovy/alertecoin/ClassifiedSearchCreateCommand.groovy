package alertecoin

import grails.validation.Validateable

@Validateable
class ClassifiedSearchCreateCommand {
    String name
    String url
    Integer checkIntervalInMinutes
    Integer hourOfDay

    static constraints = {
        name blank: false
        url blank: false, validator: {
            it.startsWith("http://www.leboncoin.fr/") || it.startsWith("https://www.leboncoin.fr/") ||
                    it.startsWith("http://leboncoin.fr/") || it.startsWith("https://leboncoin.fr/")
        }
        hourOfDay nullable: true, min: 0, max: 23
        checkIntervalInMinutes nullable: true, validator: { val, obj -> val in [5, 10, 15, 30, 60, 120, 240, 1440] }
    }
}