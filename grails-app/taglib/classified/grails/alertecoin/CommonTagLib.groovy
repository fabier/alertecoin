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

    def removeDoubleBr = { attrs, body ->
        String value = attrs.value
        if (value != null) {
            out << raw(value.replaceAll("(\\s*<br.?/?>\\s*){2,}", "<br/>"))
        }
    }
}
