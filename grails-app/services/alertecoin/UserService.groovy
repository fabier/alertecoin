package alertecoin

import grails.transaction.Transactional

@Transactional
class UserService {

    boolean isAdmin(User user) {
        user?.authorities?.any { it.authority == "ROLE_ADMIN" }
    }
}
