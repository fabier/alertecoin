package alertecoin

class ClassifiedExtra extends BaseDomain {

    Key key

    String value

    static belongsTo = Classified

    static constraints = {
        key nullable: false
        value nullable: true
    }

    static mapping = {
        value type: "text"
    }
}
