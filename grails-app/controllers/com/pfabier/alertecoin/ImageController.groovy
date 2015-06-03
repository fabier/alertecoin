package com.pfabier.alertecoin

import org.springframework.security.access.annotation.Secured

@Secured(['permitAll'])
class ImageController {

    def get(long id) {
        Image image = Image.get(id)
        response.outputStream << image.data
    }
}
