package classified.grails.alertecoin

import alertecoin.ClassifiedExtra

class AlerteCoinTagLib {
    static defaultEncodeAs = [taglib: 'html']
    //static encodeAsForTags = [tagName: [taglib:'html'], otherTagName: [taglib:'none']]

    def getExtra = { attrs, body ->
        if (attrs.extras && attrs.name) {
            def classifiedExtras = attrs.extras
            String keyname = attrs.name
            ClassifiedExtra classifiedExtra = classifiedExtras?.find {
                keyname.equals(it.key.name)
            }
            if (classifiedExtra != null) {
                out << raw(classifiedExtra.value)
            }
        }
    }
}
