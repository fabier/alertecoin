package alertecoin

import org.springframework.security.access.annotation.Secured

@Secured("hasRole('ROLE_ADMIN')")
class AdminController {

    def index() {
        render view: "index"
    }
}
