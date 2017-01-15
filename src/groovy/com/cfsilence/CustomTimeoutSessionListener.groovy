package com.cfsilence

import com.pi4j.io.gpio.PinState
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.springframework.security.core.context.SecurityContext

import javax.servlet.http.HttpSessionEvent
import javax.servlet.http.HttpSessionListener

class CustomTimeoutSessionListener implements HttpSessionListener {

    GpioHandler gpioHandler
    GrailsApplication grailsApplication

    @Override
    void sessionCreated(HttpSessionEvent httpSessionEvent) {
        println 'session created'
        httpSessionEvent.session.maxInactiveInterval = 60*20
    }

    @Override
    void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        println 'session destroyed'
        SecurityContext ctx = httpSessionEvent.session.getAttribute('SPRING_SECURITY_CONTEXT')
        if( ctx?.authentication?.authenticated ) {
            println 'turning off led'
            gpioHandler.setOutputPinState(grailsApplication.config.com.cfsilence.gpio.YLed, PinState.LOW)
        }
    }
}