package com.cfsilence

/**
 * Created by Todd Sharp on 12/22/2016.
 */

import com.pi4j.wiringpi.Gpio
import com.pi4j.wiringpi.GpioUtil

/*
    inspired by https://github.com/majauskas/433MHz
    which seemed to be inspired by https://github.com/timleland/rfoutlet
    but this one actually works!
 */

class RfTransmit {

    private final int nRepeatTransmit = 100
    /* bumped up from the C++ version which had 10 - that doesn't quite seem to be enough for java */
    private int nPulseLength = 350
    private int nProtocol = 1
    private int pin

    public RfTransmit(int nProtocol, int pin) {
        this.nProtocol = nProtocol
        if( !pin ) {
            throw new Exception('You must pass the GPIO pin number for the RF Transmitter.')
        }
        this.pin = pin

        if (nProtocol == 1)
            nPulseLength = 350
        else if (nProtocol == 2)
            nPulseLength = 650
        else if (nProtocol == 3)
            nPulseLength = 100
    }

    def setPulseLength(int nPulseLength) {
        this.nPulseLength = nPulseLength
    }

    def send(int code, int length) {
        //println code + ' at pulse length ' + nPulseLength + ' using protocol ' + nProtocol
        send(dec2binWzerofill(code, length))
    }


    def send(char[] sCodeWord) {
        for (int nRepeat = 0; nRepeat < nRepeatTransmit; nRepeat++) {
            int i = 0
            while (sCodeWord[i] != '\0') {
                switch (sCodeWord[i]) {
                    case '0':
                        send0()
                        break
                    case '1':
                        send1()
                        break
                }
                i++
            }
            sendSync()
        }
    }

    def dec2binWzerofill(int Dec, int bitLength) {
        return toBinary(Dec, bitLength)
    }

    def toBinary(int dec, int bitLength) {
        String strBin = ""
        String binary = Integer.toBinaryString(dec)
        for (int i = 0; i < (bitLength - binary.length()); i++) {
            strBin += "0"
        }
        strBin += binary + "\0"
        char[] bin = strBin.toCharArray()
        return bin
    }

    /**
     * Sends a "Sync" Bit
     *                       _
     * Waveform Protocol 1: | |_______________________________
     *                       _
     * Waveform Protocol 2: | |__________
     */
    def sendSync() {
        if (nProtocol == 1)
            transmit(1, 31)
        else if (nProtocol == 2)
            transmit(1, 10)
        else if (nProtocol == 3)
            transmit(1, 71)
    }

    /**
     * Sends a "0" Bit
     *                       _
     * Waveform Protocol 1: | |___
     *                       _
     * Waveform Protocol 2: | |__
     */
    def send0() {
        if (nProtocol == 1)
            transmit(1, 3)
        else if (nProtocol == 2)
            transmit(1, 2)
        else if (nProtocol == 3)
            transmit(4, 11)
    }

    /**
     * Sends a "1" Bit
     *                       ___
     * Waveform Protocol 1: |   |_
     *                       __
     * Waveform Protocol 2: |  |_
     */
    def send1() {
        if (nProtocol == 1)
            transmit(3, 1)
        else if (nProtocol == 2)
            transmit(2, 1)
        else if (nProtocol == 3)
            transmit(9, 6)
    }

    def enableTransmit() {
        GpioUtil.export(pin, GpioUtil.DIRECTION_OUT)
        Gpio.pinMode(pin, Gpio.OUTPUT)
    }

    def transmit(int nHighPulses, int nLowPulses) {
        Gpio.digitalWrite(pin, 1)
        Gpio.delayMicroseconds(nPulseLength * nHighPulses)
        Gpio.digitalWrite(pin, 0)
        Gpio.delayMicroseconds(nPulseLength * nLowPulses)
    }


}