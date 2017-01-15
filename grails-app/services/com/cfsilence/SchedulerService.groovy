package com.cfsilence

import grails.plugins.quartz.QuartzMonitorJobFactory
import org.quartz.Scheduler
import org.quartz.Trigger
import grails.transaction.Transactional

import static org.quartz.impl.matchers.GroupMatcher.jobGroupEquals

@Transactional
class SchedulerService {

    Scheduler quartzScheduler

    def listJobs = {
        def jobsList = []
        def listJobGroups = quartzScheduler.jobGroupNames
        listJobGroups?.each {jobGroup ->
            quartzScheduler.getJobKeys(jobGroupEquals(jobGroup))?.each {jobKey ->
                def jobName = jobKey.name
                List<Trigger> triggers = quartzScheduler.getTriggersOfJob(jobKey)
                if (triggers) {
                    triggers.each {trigger ->
                        def currentJob = createJob(jobGroup, jobName, jobsList, trigger.key.name)
                        currentJob.trigger = trigger
                        def state = quartzScheduler.getTriggerState(trigger.key)
                        currentJob.triggerStatus = Trigger.TriggerState.find {
                            it == state
                        } ?: "UNKNOWN"
                    }
                } else {
                    createJob(jobGroup, jobName, jobsList)
                }
            }
        }
        return [jobs: jobsList, now: new Date(), schedulerInStandbyMode: quartzScheduler.isInStandbyMode()]
    }

    private createJob(String jobGroup, String jobName, List jobsList, String triggerName = "") {
        def currentJob = [group: jobGroup, name: jobName] + (QuartzMonitorJobFactory.jobRuns[triggerName] ?: [:])
        jobsList << currentJob
        return currentJob
    }
}
