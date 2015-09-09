package alertecoin

class Image extends BaseDomain {

    /**
     * Le contenu de l'image
     */
    byte[] data

    /**
     * L'url dont elle est extraite
     */
    String url

    static constraints = {
        data nullable: true
        url nullable: true
        lastUpdated nullable: true
        dateCreated nullable: true
    }
}
