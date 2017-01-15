package com.cfsilence

import com.pi4j.io.gpio.PinState
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.springframework.context.ApplicationEvent
import org.springframework.context.ApplicationListener
import org.springframework.security.access.event.AuthorizationFailureEvent
import org.springframework.security.authentication.event.AuthenticationSuccessEvent
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.session.SessionDestroyedEvent

class ApplicationEventListener implements ApplicationListener {
    GpioHandler gpioHandler
    GrailsApplication grailsApplication

    public void onApplicationEvent(ApplicationEvent appEvent) {
        //println appEvent.class.name
        if (appEvent instanceof AuthenticationSuccessEvent) {
            AuthenticationSuccessEvent event = (AuthenticationSuccessEvent) appEvent
            def auth = event.authentication
            if (auth.authenticated) {
                println 'turning on led'
                gpioHandler.setOutputPinState(grailsApplication.config.com.cfsilence.gpio.YLed, PinState.HIGH)
            }
            println 'AuthenticationSuccessEvent'
        }
        if (appEvent instanceof AuthorizationFailureEvent) {
            AuthorizationFailureEvent event = (AuthorizationFailureEvent) appEvent
            def auth = event.authentication
            if (auth.authenticated) {
                println 'turning off led'
                gpioHandler.setOutputPinState(grailsApplication.config.com.cfsilence.gpio.YLed, PinState.LOW)
            }
            println 'AuthorizationFailureEvent'
        }
    }
}

