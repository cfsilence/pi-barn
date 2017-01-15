
import com.cfsilence.RfService
import com.cfsilence.GpioHandler
import com.cfsilence.SchedulerService
import com.cfsilence.WeatherService
import com.pi4j.io.gpio.PinPullResistance
import com.pi4j.io.gpio.PinState
import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import org.codehaus.groovy.grails.commons.GrailsApplication

/**
 * Created by Todd Sharp on 12/22/2016.
 */
@Secured('ROLE_ADMIN')
class PageController {

    GrailsApplication grailsApplication
    WeatherService weatherService
    SchedulerService schedulerService
    RfService rfService
    GpioHandler gpioHandler

    def index(){
    }

    def ajaxWeather(){
        def sensorResult = weatherService.getTemperatureAndHumidity()
        def temperatureAndHumidity = !sensorResult.readingFailed ? weatherService.parseTemperatureAndHumidity(sensorResult.output) : null
        render( temperatureAndHumidity as JSON )
    }

    def ajaxScheduledJobs() {
        render( schedulerService.listJobs() as JSON )
    }

    def ajaxFlameSensor(){
        render( [present: gpioHandler.flameSensor.getState() == PinState.HIGH] as JSON )
    }

    def ajaxGetPinState(){
        def pinNumber = params.int('pinNumber')
        def resistance = params?.resistance && params?.resistance.toLowerCase() == 'up' ? PinPullResistance.PULL_UP : PinPullResistance.PULL_DOWN
        render( [state: gpioHandler.getInputPinState(pinNumber, resistance)] as JSON )
    }

    def ajaxSetPinState(){
        def pinNumber = params.int('pinNumber')
        def pinStateCode = params.pinStateCode
        def pinState = pinStateCode.toLowerCase() == PinState.HIGH.name.toLowerCase() ? PinState.HIGH : PinState.LOW
        render( [state: gpioHandler.setOutputPinState(pinNumber, pinState)] as JSON )
    }

    def ajaxSendRfCode(){
        def outletNum = params.long('outletNum')
        def state = params.long('outletState')
        def outlet = 'outlet' + outletNum
        def config = grailsApplication.config.com.cfsilence.rf[outlet]
        rfService.sendCode(state == 1 ? config.on : config.off, config.pulseLength)
        render([sent: true] as JSON)
    }

    def ajaxLed(){
        def color = params.color
        switch (color.toLowerCase()) {
            case 'red':
                gpioHandler.red(true)
                break;
            case 'green':
                gpioHandler.green(true)
                break;
            case 'blue':
                gpioHandler.blue(true)
                break;
            case 'off':
                gpioHandler.red(false)
                gpioHandler.green(false)
                gpioHandler.blue(false)
                break;
        }
        render([success: true] as JSON)
    }

    @Secured('IS_AUTHENTICATED_FULLY')
    def checkSessionTimeout() {
        def inactiveMS = session.maxInactiveInterval * 1000
        def expiresAt = new Date(session.lastHit.getTime() + inactiveMS)
        def expiresInSeconds = ( ( new Date(session.lastHit.getTime() + inactiveMS).getTime() - new Date().getTime() ) / 1000 ).toInteger()
        def expired = expiresAt.getTime() < new Date().getTime()
        if( expired ) session.invalidate()
        render ([expiresAt: expiresAt, expiresInSeconds: expiresInSeconds,  expired: expired] as JSON)
    }
}
