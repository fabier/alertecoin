package alertecoin

import grails.transaction.Transactional

@Transactional
class ImageService {

    /**
     * Retourne l'image contenue dans l'URL passée en paramètre.
     * Potentiellement, si l'url ne retourne pas de contenu (une erreur 404 par exemple), alors on retourne <code>null</code>
     * @param url
     * @return
     */
    Image getImageByURL(String url) {
        Image image = Image.findByUrl(url)
        if (image == null || image.data == null) {
            byte[] data = getBytes(url)
            if (data != null) {
                // Si on a des données, alors on peut créer l'image si elle n'existe pas
                // et on peut lui affecter les données de l'image contenue dans l'url
                image = image ?: new Image(url: url)
                image.data = data
                image.save()
            }
        }
        // L'image retournée peut être nulle
        return image
    }

    private static def getBytes(String url) {
        try {
            return new URL(url).getBytes()
        } catch (IOException e) {
            log.warn("Impossible to fetch image from URL : ${url}", e)
            return null
        }
    }
}
