package alertecoin

import grails.transaction.Transactional

@Transactional
class UserService {

    def springSecurityService

    boolean isAdmin(User user = null) {
        user = user ?: springSecurityService.currentUser
        user?.authorities?.any { it.authority == "ROLE_ADMIN" }
    }
}
