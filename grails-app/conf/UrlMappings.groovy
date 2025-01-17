class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?" {
            constraints {
                // apply constraints here
                id(matches: /\d*/)
            }
        }

        "/"(controller: "common", action: "index")

        "404"(view: '404')
        "500"(view: 'error')
    }
}
