/**
 * Created by Todd Sharp on 12/22/2016.
 */
barnApp.controller('BarnController', [
    '$scope',
    '$timeout',
    'barnService',
    '$rootScope',
    '$window',
    function ($scope, $timeout, barnService, $rootScope, $window) {
        $scope.foo = 'angular is installed!'
        $scope.weather = null
        $scope.scheduledJobs = null
        $scope.flameSensorState = false
        $scope.sensorStates = {door: null, flame: null}

        $scope.formatFireTime = function(dt) {
            return moment(dt).calendar()
        }

        $scope.openWebcamFeedModal = function(){
            var modal = $('#webcamModal')
            modal.find('.modal-body').html('<img id="webcamStream" src="'+ PiBarn.webcamFeed +'" style="width: 100%;"></img>')

            modal.modal('show')
        }

        $('#play').on('click', function(){
            $('#webcamStream').get(0).play()
        })

        $('#webcamModal').on('hidden.bs.modal', function (e) {
            $('#webcamModal').find('.modal-body').html('')
        })

        $scope.filterRfGroupJobs = function(job) {
            return job.group == 'RfGroup' && job.trigger ? true : false
        }

        $scope.getWeather = function(){

            $('#weatherPanel').find('.fa-refresh').addClass('fa-spin')

            barnService.getWeather().success(function (result) {
                $scope.weather = result
                $('#weatherPanel').find('.fa-refresh').removeClass('fa-spin')
            }).error(function (data) {
                alert('There was an error retrieving weather. Please try again.')
                $('#weatherPanel').find('.fa-refresh').removeClass('fa-spin')
            })
        }

        $scope.getScheduledJobs= function(){
            barnService.getScheduledJobs().success(function (result) {
                $scope.scheduledJobs = result
                console.log(result)
            }).error(function (data) {
                alert('There was an error retrieving scheduled jobs. Please try again.')
            })
        }

        $scope.toggleOutlet = function(outlet, state){
            var  obj = {outletNum: outlet, outletState: state}
            barnService.sendRfCode(obj).success(function (result) {
                alert('Code sent to outlet')
            }).error(function (data) {
                alert('There was an error sending the code. Please try again.')
            })
        }

        $scope.getFlameSensor = function(){
            $('tr[data-sensor="Flame"]').find('.fa-refresh').removeClass('hide')
            barnService.getFlameSensor().success(function (result) {
                $scope.sensorStates['flame'] = { name: 'Flame', state: result.present ? 'Danger' : 'OK', icon: 'fa-fire text-danger'}
                $('tr[data-sensor="Flame"]').find('.fa-refresh').addClass('hide')
            }).error(function (data) {
                alert('There was an error retrieving flame sensor state. Please try again.')
                $('tr[data-sensor="Flame"]').find('.fa-refresh').addClass('hide')
            })
        }

        $scope.getDoorSensorState = function() {
            $('tr[data-sensor="Door"]').find('.fa-refresh').removeClass('hide')
            barnService.getPinState(PiBarn.gpioPins.ReedSwitch0, PiBarn.resistances.UP).success(function (result) {
                $scope.sensorStates['door'] = { name: 'Door', state: result.state.name == PiBarn.pinStates.HIGH ? 'Open' : 'Closed', icon: 'fa-building'}
                $('tr[data-sensor="Door"]').find('.fa-refresh').addClass('hide')
            }).error(function (data) {
                alert('There was an error retrieving door sensor state. Please try again.')
                $('tr[data-sensor="Door"]').find('.fa-refresh').addClass('hide')
            })
        }

        $('#weatherPanel').on('click', '.fa-refresh', function(){
            $scope.getWeather()
        })

        $('#sensorPanel').on('click', '.fa-refresh', function(){
            $scope.refreshSensorStates()
        })

        $scope.refreshSensorStates = function(){
            $scope.getDoorSensorState()
            $scope.getFlameSensor()
        }

        $scope.refreshSensorStates()
        $scope.getWeather()
        $scope.getScheduledJobs()
        $scope.getFlameSensor()

    }
]);