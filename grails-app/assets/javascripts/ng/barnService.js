/**
 * Created by Todd Sharp on 12/22/2016.
 */
barnApp.service('barnService', ['$http', '$window',
    function ($http, $window) {
        _this = this
        this.getWeather = function () {
            return $http.get($window.PiBarn.baseURL + 'page/ajaxWeather')
        }
        this.getScheduledJobs = function () {
            return $http.get($window.PiBarn.baseURL + 'page/ajaxScheduledJobs')
        }
        this.getFlameSensor = function () {
            return $http.get($window.PiBarn.baseURL + 'page/ajaxFlameSensor')
        }
        this.saveFoo = function (obj) {
            return $http.post($window.PiBarn.baseURL + 'data/ajaxSaveNewLink', obj)
        }
        this.getPinState = function (pinNumber, resistance) {
            if( typeof resistance == 'undefined' ) resistance = $window.PiBarn.resistances.DOWN
            return $http.get($window.PiBarn.baseURL + 'page/ajaxGetPinState?pinNumber=' + pinNumber + '&resistance=' + resistance)
        }
        this.setPinState = function (obj) {
            return $http.post($window.PiBarn.baseURL + 'page/ajaxSetPinState', obj)
        }
        this.sendRfCode = function (obj) {
            return $http.post($window.PiBarn.baseURL + 'page/ajaxSendRfCode', obj)
        }
        this.led = function(color) {
            return $http.get($window.PiBarn.baseURL + 'page/ajaxLed?color=' + color)
        }
    }
]);