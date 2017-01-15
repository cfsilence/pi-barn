<%@ page import="com.pi4j.io.gpio.PinPullResistance; com.pi4j.io.gpio.PinState;grails.converters.JSON;" %>
<g:javascript>
var PiBarn = {}
PiBarn.baseURL = '${grailsApplication.config.grails.serverURL}/'
PiBarn.gpioPins = ${raw((grailsApplication.config.com.cfsilence.gpio as JSON) as String)}
PiBarn.pinStates = ${raw(([HIGH: PinState.HIGH.name, LOW: PinState.LOW.name] as JSON) as String)}
PiBarn.resistances = ${raw(([UP: PinPullResistance.PULL_UP.name, DOWN: PinPullResistance.PULL_DOWN.name] as JSON) as String)}
PiBarn.webcamFeed = 'http://192.168.0.190:9900/stream/video.mjpeg'
PiBarn.RED = 'red'
PiBarn.GREEN = 'green'
PiBarn.BLUE = 'blue'
PiBarn.OFF = 'off'
<g:if test="${actionName == 'index'}">
$(document).ready(function(){
        var modal = '<div class="modal" id="timeoutModal" tabindex="-1" role="dialog"><div class="modal-dialog" role="document"><div class="modal-content"><div class="modal-header"><button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button><h4 class="modal-title"><i class="fa fa-warning"></i> Session Warning</h4></div><div class="modal-body"></div><div class="modal-footer"><button type="button" class="btn btn-primary" id="renewSessionBtn" data-dismiss="modal">Keep Alive</button><button type="button" class="btn btn-default" id="cancelSessionBtn" data-dismiss="modal">Close</button></div></div></div>'
        var timeoutModal = $(modal)

        timeoutModal.modal({ keyboard: false, backdrop: 'static', show: false})
        $('body').append(timeoutModal)

        $('body').on('click', '#cancelSessionBtn', function(){
            timeoutModal.modal('hide')
        })

        $('body').on('click', '#renewSessionBtn', function(){
            $.ajax({
                url: PiBarn.baseURL,
                success: function(){
                    timeoutModal.modal('hide')
                }
            })
        })

        sessionTimeoutInterval = setInterval(function () {
            timeoutModal.modal('hide')

            $.ajax({
                url: 'page/checkSessionTimeout',
                headers: {'x-pibarn-session-check': true},
                success: function (data, status, xhr) {
                    if (data.expired) {
                        location.href = PiBarn.baseURL + 'login/auth'
                    }
                    if (data.expiresInSeconds <= 300) {
                        var expires = parseInt(data.expiresInSeconds / 60) + ' minutes'
                        if (parseInt(data.expiresInSeconds / 60) == 1) expires = '1 minute'
                        if (data.expiresInSeconds <= 60) expires = parseInt(data.expiresInSeconds) + ' seconds'
                        timeoutModal.find('.modal-body').html('Your session will expire in ' + expires + '.  Would you like to stay logged in?')
                        timeoutModal.modal('show')
                    }
                },
                error: function () {
                    clearInterval(sessionTimeoutInterval)
                    location.href = PiBarn.baseURL + 'login/auth'
                }
            })
        }, 1000 * 15)
})
</g:if>

</g:javascript>