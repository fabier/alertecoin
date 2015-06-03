package com.pfabier.alertecoin

class Alert {

    /**
     * Nom de la recherche
     */
    String name

    /**
     * URL correspondant à la recherche
     */
    String url

    /**
     * Date de dernière vérification de la liste des annonces associées a cette recherche
     */
    Date lastCheckedDate

    /**
     * Date de prochaine vérification
     */
    Date nextCheckDate

    /**
     * Intervalle entre deux vérifications
     */
    Integer checkIntervalInMinutes

    /**
     * Date de l'annonce la plus récente associée à cette recherche
     */
    Date mostRecentClassifiedDate

    /**
     * Etat de la recherche
     */
    State state

    /**
     * Liste des annonces associées à cette recherche
     */
    List<Classified> classifieds

    Date dateCreated

    Date lastUpdated

    static hasMany = [classifieds: Classified]

    static belongsTo = [user: User]

    static transients = ['proxyUrl']

    static constraints = {
        name nullable: true
        url nullable: true
        state nullable: true
        lastCheckedDate nullable: true
        mostRecentClassifiedDate nullable: true
        nextCheckDate nullable: true
        checkIntervalInMinutes nullable: true
        lastUpdated nullable: true
        dateCreated nullable: true
    }

    static mapping = {
        url sqlType: "text"
    }

    String getProxyUrl() {
        return url == null ? null : "https://proxy-de.hide.me/go.php?u=${URLEncoder.encode(url, "UTF-8")}&b=4"
    }
}
