import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.sns.AmazonSNSClient
import com.cfsilence.ApplicationEventListener
import com.cfsilence.CustomTimeoutSessionListener
import com.cfsilence.GpioHandler
import groovy.sql.Sql

// Place your Spring DSL code here
beans = {
    sql(Sql, ref('dataSource')){}

    grailsApplication = ref('grailsApplication')
    springSecurityService = ref('springSecurityService')

    snsClient(AmazonSNSClient) { bean ->
        bean.constructorArgs = [new BasicAWSCredentials(grailsApplication.config.awssdk.sns.accessKey.toString(), grailsApplication.config.awssdk.sns.secretKey.toString())]
    }

    gpioHandler(GpioHandler) { bean ->
        bean.scope = 'singleton'
        rPinNumber = grailsApplication.config.com.cfsilence.gpio.RLed
        gPinNumber = grailsApplication.config.com.cfsilence.gpio.GLed
        bPinNumber = grailsApplication.config.com.cfsilence.gpio.BLed
        flamePinNumber = grailsApplication.config.com.cfsilence.gpio.FlameSensor
    }

    customTimeoutSessionListener(CustomTimeoutSessionListener) {
        gpioHandler = ref('gpioHandler')
        grailsApplication = ref('grailsApplication')
    }

    applicationEventListener(ApplicationEventListener) {
        gpioHandler = ref('gpioHandler')
        grailsApplication = ref('grailsApplication')
    }
}
