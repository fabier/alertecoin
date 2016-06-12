package alertecoin

import org.springframework.security.access.annotation.Secured

@Secured("hasRole('ROLE_ADMIN')")
class UserController extends grails.plugin.springsecurity.ui.UserController {

    def show(long id){
        User user = User.get(id)
        render view: "show", model: [user: user]
    }
}
