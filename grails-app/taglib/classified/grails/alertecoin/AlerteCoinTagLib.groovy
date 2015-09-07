package classified.grails.alertecoin

import com.pfabier.alertecoin.ClassifiedExtra

class AlerteCoinTagLib {
    static defaultEncodeAs = [taglib: 'html']
    //static encodeAsForTags = [tagName: [taglib:'html'], otherTagName: [taglib:'none']]

    def getExtra = { attrs, body ->
        if (attrs.extras && attrs.name) {
            List<ClassifiedExtra> classifiedExtras = attrs.extras
            ClassifiedExtra classifiedExtra = classifiedExtras?.find {
                it.key.name == attrs.name
            }
            if (classifiedExtra != null) {
                out << raw(classifiedExtra.value)
            }
        }
    }
}
