package com.cfsilence

import com.amazonaws.services.sns.AmazonSNSClient
import com.amazonaws.services.sns.model.MessageAttributeValue
import com.amazonaws.services.sns.model.PublishRequest
import com.pi4j.io.gpio.GpioFactory
import com.pi4j.io.gpio.GpioPinDigitalInput
import com.pi4j.io.gpio.PinPullResistance
import com.pi4j.io.gpio.PinState
import com.pi4j.io.gpio.RaspiPin
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent
import com.pi4j.io.gpio.event.GpioPinListenerDigital

/**
 * Created by Todd Sharp on 12/27/2016.
 */

class FlameSensorListener {
    Integer pinNumber
    AmazonSNSClient snsClient
    List smsDestinations
    GpioPinDigitalInput flameSensor

    def notified = []

    FlameSensorListener() {
    }

    def init() {
        println 'fire sensor init'
        def gpio = GpioFactory.getInstance()
        def raspiPin = RaspiPin.getPinByAddress(pinNumber)
        flameSensor = gpio.provisionDigitalInputPin(raspiPin, PinPullResistance.PULL_DOWN)
        //flameSensor.setDebounce(1000)
        flameSensor.setShutdownOptions(true)
        flameSensor.addListener( new GpioPinListenerDigital() {
            @Override
            void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent gpioPinDigitalStateChangeEvent) {
                println 'GPIO PIN STATE CHANGE ' + new Date().toString()
                println gpioPinDigitalStateChangeEvent
                println gpioPinDigitalStateChangeEvent.getState()
                if( gpioPinDigitalStateChangeEvent.state == PinState.HIGH ) {
                    // shits on fire yo
                    def message = "Flame sensor listener detected flame state."
                    def smsAttributes = [:]
                    smsAttributes.put("AWS.SNS.SMS.SMSType", new MessageAttributeValue().withStringValue("Transactional").withDataType("String"))

                    smsDestinations.each { dest ->
                        if( notified.indexOf(dest) == -1 ) {
                            notified << dest
                            def publishRequest = new PublishRequest()
                            publishRequest.withMessage(message)
                            publishRequest.message = message
                            publishRequest.phoneNumber = dest
                            publishRequest.messageAttributes = smsAttributes
                            //snsClient.publish(publishRequest)
                            //println 'sending SMS to ' + dest
                        }
                    }
                }
                else {
                    // state is low, not detecting flame
                    // reset the notified list
                    notified = []
                }
            }
        })
        println 'listener initialized'
        while(true) {
            sleep(500)
        }
    }

    def shutdown() {
        def gpio = GpioFactory.getInstance()
        gpio.shutdown()
    }
}