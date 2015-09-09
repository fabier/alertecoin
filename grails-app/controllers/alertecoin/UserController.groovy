package alertecoin

import org.springframework.security.access.annotation.Secured

@Secured("hasRole('ROLE_ADMIN')")
class UserController extends grails.plugin.springsecurity.ui.UserController {

    def index() {
        def users = User.all.sort { a, b ->
            a.username.compareToIgnoreCase(b.username)
        }
        render view: "index", model: [users: users]
    }

    def show(long id){
        User user = User.get(id)
        render view: "show", model: [user: user]
    }
}
