package com.cfsilence

import com.pi4j.io.gpio.PinState
import grails.transaction.Transactional

@Transactional
class FlameService {

    GpioHandler gpioHandler

    def isFlamePresent(){
        return gpioHandler.flameSensor.getState() == PinState.HIGH
    }
}
