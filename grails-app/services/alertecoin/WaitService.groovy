package alertecoin

import grails.transaction.Transactional

@Transactional
class WaitService {

    long waitAtLeast(long waitinms, long lasttime) {
        if (lasttime > 0 && lasttime <= System.currentTimeMillis() && System.currentTimeMillis() <= lasttime + waitinms) {
            Thread.sleep(waitinms - (System.currentTimeMillis() - lasttime))
        }
        return System.currentTimeMillis()
    }
}
