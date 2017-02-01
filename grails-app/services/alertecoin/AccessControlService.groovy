package alertecoin

import grails.transaction.Transactional

@Transactional
class AccessControlService {

    AlertService alertService
    UserService userService

    boolean hasRights(User user, Alert alert) {
        alertService.isOwner(alert, user) || userService.isAdmin(user)
    }
}
