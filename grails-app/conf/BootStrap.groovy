import com.cfsilence.CustomTimeoutSessionListener
import com.cfsilence.GpioHandler
import com.cfsilence.Role
import com.cfsilence.User
import com.cfsilence.UserRole
import groovy.sql.Sql
import org.codehaus.groovy.grails.commons.GrailsApplication

import java.time.LocalDateTime

class BootStrap {

    GpioHandler gpioHandler
    Sql sql
    GrailsApplication grailsApplication
    CustomTimeoutSessionListener customTimeoutSessionListener

    def init = { servletContext ->
        gpioHandler.init()

        if (LocalDateTime.now().hour >= 6) {
            RfSchedulerJob.triggerNow()
        }

        if( !User.findByUsername('admin') ) {
            def user = new User('admin', 'pibarn').save(flush: true, failOnError: true)
            def role = Role.findByAuthority(Role.ROLE_ADMIN)
            if( !role ) {
                role = new Role(Role.ROLE_ADMIN).save(flush: true, failOnError: true)
            }
            def userRole = new UserRole(user, role).save(flush: true, failOnError: true)
        }

        servletContext.addListener(customTimeoutSessionListener)

        gpioHandler.green(true)
    }

    def destroy = {
        gpioHandler.green(false)
        gpioHandler.shutdown()
    }

}
