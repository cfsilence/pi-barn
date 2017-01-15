package com.cfsilence

import grails.transaction.Transactional

@Transactional
class WeatherService {

    def grailsApplication

    Map getTemperatureAndHumidity() {
        def pin = grailsApplication.config.com.cfsilence.gpio.AM2302Pin
        /* cheesy way to do this, but, Java has issues reading the AM2302 sensors because of timing */
        def process = "python /home/pi/adafruit/Adafruit_Python_DHT/examples/AdafruitDHT.py 2302 ${pin}".execute()
        StringBuilder sensorOutput = new StringBuilder()
        StringBuilder sensorError = new StringBuilder()
        def s

        BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
        BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));

        // read the output from the command
        while ((s = stdInput.readLine()) != null) {
            sensorOutput.append(s)
        }

        // read any errors from the attempted command
        while ((s = stdError.readLine()) != null) {
            sensorError.append(s)
        }

        def hasError = sensorOutput.contains('Failed')
        return [error: sensorError.toString(), output: sensorOutput.toString(), readingFailed: hasError]
    }

    Map parseTemperatureAndHumidity(String tempAndHumidity){
        def asList = tempAndHumidity.split('  ')
        def result = [temperature:'0', humidity:'0%']
        if( !asList.size() ) return result
        def tempC = asList[0].split('=').last().replace('*', '')
        result.temperature = tempC ? celsiusToFarenheit(tempC) : 0
        result.humidity = asList[1].split('=').last()
        return result
    }

    /**
     * celsiusToFarenheit - T(°F) = 20°C × 9/5 + 32 = 68 °F
     * @param degrees
     * @return
     */
    def celsiusToFarenheit(degrees) {
        return ((degrees as Float) * (9/5) + 32).round(2)
    }
}
