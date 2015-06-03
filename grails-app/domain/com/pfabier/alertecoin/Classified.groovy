package com.pfabier.alertecoin

class Classified {

    /**
     * Identifiant de la petite annonce
     */
    Long externalId

    /**
     * Titre de l'annonce
     */
    String name

    /**
     * Description de l'annonce
     */
    String description

    /**
     * URL pour accéder à l'annonce
     */
    String url

    /**
     * Prix de l'objet
     */
    Integer price

    /**
     * Date de dépot de l'annonce
     */
    Date date

    Date dateCreated

    Date lastUpdated

    static hasMany = [images: Image]

    static constraints = {
        name nullable: true
        url nullable: true
        description nullable: true
        externalId nullable: true
        price nullable: true
        date nullable: true
        lastUpdated nullable: true
        dateCreated nullable: true
    }

    static mapping = {
        name sqlType: "text"
        url sqlType: "text"
        description sqlType: "text"
    }
}
