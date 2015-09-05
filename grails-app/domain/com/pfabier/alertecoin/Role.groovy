package com.pfabier.alertecoin

class Role extends BaseDomain {

    String authority

    static mapping = {
        cache true
    }

    static constraints = {
        authority blank: false, unique: true
        lastUpdated nullable: true
        dateCreated nullable: true
    }
}
