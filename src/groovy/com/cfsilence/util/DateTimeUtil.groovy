package com.cfsilence.util

import groovy.json.JsonSlurper

class DateTimeUtil {

    static def sunriseSunset(lat, lon) {
        def result = "http://api.sunrise-sunset.org/json?lat=${lat}&lng=${lon}&date=today&formatted=0".toURL().text
        return new JsonSlurper().parseText(result)
    }

}