package com.cfsilence

import grails.transaction.Transactional

@Transactional
class RfService {

    def grailsApplication

    def sendCode(Integer code, Integer pulseLength) {
        def pin = grailsApplication.config.com.cfsilence.gpio.RfPin
        def sender = new RfTransmit(1, pin)
        sender.enableTransmit()
        sender.setPulseLength(pulseLength)
        sender.send(code, 24)
    }
}
