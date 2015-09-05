package com.pfabier.alertecoin

class ClassifiedExtra extends BaseDomain {

    Key key

    String value

    static constraints = {
        key nullable: false
        value nullable: false
    }

    static mapping = {
        value type: "text"
    }
}
