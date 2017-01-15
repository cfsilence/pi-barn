package com.cfsilence

class PiBarnSessionFilters{
    def filters = {
        // on all requests EXCEPT checkSessionTimeout set the session.lastHit = new Date()
        sessionLastHit(controller: 'page', action: 'checkSessionTimeout', invert: true) {
            before = {
                if( actionName != 'checkSessionTimeout' ) {
                    def session = request.session
                    session.lastHit = new Date()
                }
            }
        }
    }
}