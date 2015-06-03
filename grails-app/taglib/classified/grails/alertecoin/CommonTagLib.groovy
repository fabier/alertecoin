package classified.grails.alertecoin

class CommonTagLib {
    static defaultEncodeAs = [taglib: 'html']
    //static encodeAsForTags = [tagName: [taglib:'html'], otherTagName: [taglib:'none']]

    def applicationName = { attrs, body ->
        out << "AlerteCoin"
    }

    def getYear = { attrs, body ->
        out << formatDate(date: new Date(), format: "yyyy")
    }


}
