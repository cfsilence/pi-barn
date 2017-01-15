package com.cfsilence

import com.pi4j.io.gpio.*

/**
 * Created by Todd Sharp on 12/30/2016.
 */

class GpioHandler {
    Integer rPinNumber
    Integer gPinNumber
    Integer bPinNumber
    Integer flamePinNumber

    GpioPinDigitalOutput r
    GpioPinDigitalOutput g
    GpioPinDigitalOutput b
    GpioPinDigitalInput flameSensor
    GpioController gpio

    List provisionedPins = []

    GpioHandler() {
    }

    def init() {
        println 'gpio handler init'
        gpio = GpioFactory.getInstance()
        def rRaspiPin = RaspiPin.getPinByAddress(rPinNumber)
        def gRaspiPin = RaspiPin.getPinByAddress(gPinNumber)
        def bRaspiPin = RaspiPin.getPinByAddress(bPinNumber)
        def flameRaspiPin = RaspiPin.getPinByAddress(flamePinNumber)

        r = gpio.provisionDigitalOutputPin(rRaspiPin)
        g = gpio.provisionDigitalOutputPin(gRaspiPin)
        b = gpio.provisionDigitalOutputPin(bRaspiPin)
        flameSensor = gpio.provisionDigitalInputPin(flameRaspiPin, PinPullResistance.PULL_DOWN)

        // add them to our list of provisioned pins so that if we want to read/write from them via generic methods we won't throw an error
        provisionedPins.addAll([r,g,b,flameSensor])

        flameSensor.setShutdownOptions(true)
        r.setShutdownOptions(true)
        g.setShutdownOptions(true)
        b.setShutdownOptions(true)
    }

    def red(Boolean on) {
        g.setState(false)
        b.setState(false)
        r.setState(on ? PinState.HIGH : PinState.LOW)
    }

    def green(Boolean on) {
        r.setState(false)
        b.setState(false)
        g.setState(on ? PinState.HIGH : PinState.LOW)
    }

    def blue(Boolean on) {
        g.setState(false)
        r.setState(false)
        b.setState(on ? PinState.HIGH : PinState.LOW)
    }

    def getInputPinState(Integer pinNumber, PinPullResistance resistance=PinPullResistance.PULL_DOWN){
        def raspiPin = RaspiPin.getPinByAddress(pinNumber)
        def pin = provisionedPins.find{ it?.pin?.address == pinNumber }
        if( !pin ) {
            pin = gpio.provisionDigitalInputPin(raspiPin, resistance)
            provisionedPins << pin
        }
        def state = pin.getState()
        return state
    }

    def setOutputPinState(Integer pinNumber, PinState pinState){
        def raspiPin = RaspiPin.getPinByAddress(pinNumber)
        def pin = provisionedPins.find{ it?.pin?.address == pinNumber }
        if( !pin ) {
            pin = gpio.provisionDigitalOutputPin(raspiPin)
            provisionedPins << pin
        }
        def state = pin.setState(pinState)
        return state
    }

    def shutdown() {
        gpio.shutdown()
    }
}