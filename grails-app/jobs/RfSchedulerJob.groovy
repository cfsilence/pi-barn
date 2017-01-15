import com.cfsilence.SchedulerService
import com.cfsilence.util.DateTimeUtil
import org.codehaus.groovy.grails.commons.GrailsApplication

import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * Created by Todd Sharp on 12/28/2016.
 */
class RfSchedulerJob {

    GrailsApplication grailsApplication
    SchedulerService schedulerService

    static triggers = {
        cron name: 'rfSchedulerTrigger', cronExpression: '0 0 6 * * ?'
    }

    def group = "RfManagementGroup"
    def description = "This job runs every morning to schedule the nightly RF activities"

    def execute() {
        def sunriseSunset = DateTimeUtil.sunriseSunset(grailsApplication.config.com.cfsilence.home.lat, grailsApplication.config.com.cfsilence.home.lon)
        def sunset = OffsetDateTime.parse(sunriseSunset.results.sunset, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        def localSunset = sunset.atZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime()
        def existingJobs = schedulerService.listJobs()

        grailsApplication.config.com.cfsilence.rf.each{ k, v ->
            if( v.schedule == 'nightly' ) {
                def on = Date.from(localSunset.plusMinutes(v.onOffset).atZone(ZoneId.systemDefault()).toInstant())
                def off = Date.from(localSunset.plusMinutes(v.offOffset).atZone(ZoneId.systemDefault()).toInstant())
                if( existingJobs.jobs.find{ it?.trigger?.jobName == 'RfActionJob' && it?.trigger?.jobDataMap?.action == 'on' } ) {
                    // do nothing, it's already scheduled (we might have rebooted)
                }
                else {
                    RfActionJob.schedule(on, [outlet: k, action: 'on'])
                }
                if( existingJobs.jobs.find{ it?.trigger?.jobName == 'RfActionJob' && it?.trigger?.jobDataMap?.action == 'off' } ) {
                    // do nothing, it's already scheduled
                }
                else {
                    RfActionJob.schedule(off, [outlet: k, action: 'off'])
                }
            }
        }
        print "Job run!"
    }
}
