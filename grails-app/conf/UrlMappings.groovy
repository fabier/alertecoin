class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?(.$format)?" {
            constraints {
                // apply constraints here
            }
        }

        "/"(controller: "common", action: "index")

        "404"(view: 'error')
        "500"(view: 'error')
    }
}
