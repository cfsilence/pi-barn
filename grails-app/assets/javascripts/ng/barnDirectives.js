/**
 * Created by Todd Sharp on 12/22/2016.
 */

barnApp.directive('tempLabel', function($timeout, $sce) {
    return {
        replace: true,
        restrict: 'E',
        scope : true ,
        template: function(elem, attr){
            return '<span class="label full-width" ng-class="weatherLabelClass">{{weather.temperature}}&deg;</span>'
        },
        link: function(scope, elem, attrs) {
            console.log(scope)
            scope.$watch('weather', function(n,o){
                if( n ) {
                    var temp = n.temperature
                    if( temp >= 40 ) {
                        scope.weatherLabelClass = 'label-success'
                    }
                    if( temp < 40 && temp >= 20 ) {
                        scope.weatherLabelClass = 'label-warning'
                    }
                    if( temp < 20 ) {
                        scope.weatherLabelClass = 'label-danger'
                    }

                    console.log(scope.weatherLabelClass)
                }
            })
        }
    };
});
