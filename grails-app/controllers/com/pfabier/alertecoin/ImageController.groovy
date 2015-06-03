package com.pfabier.alertecoin

import grails.plugin.springsecurity.annotation.Secured

@Secured(['permitAll'])
class ImageController {

    def get(long id) {
        Image image = Image.get(id)
        response.outputStream << image.data
    }
}
