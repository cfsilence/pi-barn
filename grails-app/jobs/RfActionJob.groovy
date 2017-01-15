import com.cfsilence.RfService

/**
 * Created by Todd Sharp on 12/28/2016.
 */
class RfActionJob {

    RfService rfService
    def grailsApplication

    static String ON = 'on'
    static String OFF = 'off'
    static String OUTLET_0 = 'outlet0'
    
    static triggers = {
    }

    def group = "RfGroup"
    def description = "This job is dynamically scheduled to turn toggle on/off an RF outlet"

    def execute(context) {
        def outlet = context.mergedJobDataMap.get('outlet')
        def action = context.mergedJobDataMap.get('action')
        def outletConfig = grailsApplication.config.com.cfsilence.rf[outlet]
        def pulseLength = outletConfig.pulseLength
        def code = outletConfig[action]
        rfService.sendCode(code, pulseLength)
        print "Job run!"
    }
}
