package alertecoin

import org.apache.commons.lang.builder.HashCodeBuilder

class ClassifiedExtra extends BaseDomain implements Serializable {

    Classified classified

    Key key

    String value

    static constraints = {
        classified unique: 'key'
        value nullable: true
    }

    static mapping = {
        id composite: ['classified', 'key']
        value type: "text"
    }

    int hashCode() {
        def builder = new HashCodeBuilder()
        if (classified) builder.append(classified.id)
        if (key) builder.append(key.id)
        builder.toHashCode()
    }
}
