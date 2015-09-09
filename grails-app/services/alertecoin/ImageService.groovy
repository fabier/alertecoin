package alertecoin

import grails.transaction.Transactional

@Transactional
class ImageService {

    Image getImageByURL(String url) {
        Image image = Image.findOrSaveByUrl(url)
        if (image.data == null) {
            image.data = getBytes(url)
            image.save()
        }
        return image
    }

    private static def getBytes(String url) {
        return new URL(url).getBytes()
    }
}
