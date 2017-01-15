<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>pi-barn</title>
    <asset:javascript src="ng/manifest.js"/>
</head>

<body>
    <div id="barnController" ng-app="barnApp" ng-controller="BarnController">
        <div class="pad-top-20">

            <div class="row pad-bottom-20">
                <div class="col-xs-12">
                    <div class="btn-group">
                        <button id="openWebcamModalBtn" class="btn btn-primary" ng-click="openWebcamFeedModal()"><i class="fa fa-video-camera"></i> Webcam Feed</button>
                    </div>
                    <div class="btn-group">
                        <div class="dropdown">
                            <button class="btn btn-primary dropdown-toggle" type="button" id="outletDropdown" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
                                <i class="fa fa-plug"></i> Outlets
                                <span class="caret"></span>
                            </button>
                            <ul class="dropdown-menu" aria-labelledby="outletDropdown">
                                <li><a href="#" ng-click="toggleOutlet(0, 1)"><i class="fa fa-power-off text-success"></i> Outlet 0 On</a></li>
                                <li><a href="#" ng-click="toggleOutlet(0, 0)"><i class="fa fa-power-off text-muted"></i> Outlet 0 Off</a></li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="col-lg-3 col-sm-4 col-xs-12">
                    <g:set var="sensorLbl"><i class="fa fa-microchip"></i> Sensors</g:set>

                    <ui:panel title="${sensorLbl}" panelId="sensorPanel" showRefreshBtn="true">
                        <div class="table-responsive" style="min-height: 320px;">
                            <table class="table" id="sensorTable">
                                <tbody>
                                    <tr ng-repeat="sensor in sensorStates" data-sensor="{{sensor.name}}">
                                        <td class="align-middle"><i class="fa fa-refresh fa-spin hide huge-text"></i></td>
                                        <td class="huge-text"><i class="fa" ng-class="sensor.icon"></i> {{sensor.name}}:</td>
                                        <td class="huge-text text-right">{{sensor.state}}</td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </ui:panel>
                </div>

                <div class="col-lg-3 col-sm-4 col-xs-12">
                    <g:set var="weatherLbl"><i class="fa fa-cloud"></i> Weather</g:set>

                    <ui:panel title="${weatherLbl}" panelId="weatherPanel" showRefreshBtn="true">
                        <div style="min-height: 320px;">
                            <div class="table-responsive" style="min-height: 320px;">
                                <table class="table">
                                    <tbody>
                                        <tr>
                                            <td class="huge-text"><i class="fa fa-thermometer-full"></i> Temp:</td>
                                            <td class="huge-text text-right"><temp-label model="weather"></temp-label></td>
                                        </tr>
                                        <tr>
                                            <td class="huge-text"><i class="fa fa-tint text-info"></i> Humidity:</td>
                                            <td class="huge-text text-right"><span class="label label-success full-width">{{weather.humidity}}</span></td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </ui:panel>
                </div>

                <div class="col-lg-6 col-sm-4 col-xs-12">

                    <g:set var="taskLbl"><i class="fa fa-calendar-check-o"></i> Scheduled Jobs</g:set>

                    <ui:panel title="${taskLbl}">
                        <div class="table-responsive" style="min-height: 320px;">
                            <table class="table table-bordered">
                                <thead>
                                    <tr>
                                        <th>Job</th>
                                        <th>Data</th>
                                        <th>At</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr ng-repeat="job in scheduledJobs.jobs | filter:filterRfGroupJobs as filteredJobs">
                                        <td><i class="fa fa-tasks text-primary"></i> {{job.name}}</td>
                                        <td>
                                            <ul>
                                                <li ng-repeat="d in job.trigger.jobDataMap">{{d}}</li>
                                            </ul>
                                        </td>
                                        <td>{{formatFireTime(job.trigger.nextFireTime)}}</td>
                                    </tr>
                                    <tr ng-if="filteredJobs.length == 0">
                                        <td colspan="3" class="info text-center">
                                            <b>No More Pending Jobs For Today</b>
                                        </td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </ui:panel>
                </div>
            </div>
        </div>
    </div>

    <div class="modal fade" id="webcamModal" tabindex="-1" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title"><i class="fa fa-video-camera"></i> Webcam Feed</h4>
                </div>
                <div class="modal-body">
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->

    <div class="modal fade" id="helpModal" tabindex="-1" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title"><i class="fa fa-question"></i> Help</h4>
                </div>
                <div class="modal-body">
                    <h4>LEDs:</h4>
                    <div class="row">
                        <div class="col-xs-12"><i class="fa fa-circle yellow outline-text"></i> The web application has an active session.</div>
                    </div>
                    <div class="row">
                        <div class="col-xs-12"><i class="fa fa-circle green outline-text"></i> The system is online.</div>
                    </div>
                    <div class="row">
                        <div class="col-xs-12"><i class="fa fa-circle red outline-text"></i> The system has one or more alerts.</div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->


</body>
</html>
