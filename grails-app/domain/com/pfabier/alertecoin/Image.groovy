package com.pfabier.alertecoin

class Image {

    /**
     * Le contenu de l'image
     */
    byte[] data

    /**
     * L'url dont elle est extraite
     */
    String url

    Date dateCreated

    Date lastUpdated

    static constraints = {
        data nullable: true
        url nullable: true
        lastUpdated nullable: true
        dateCreated nullable: true
    }
}
