'use strict';

angular.module('thrApp')
    .controller('LocationDetailController', function ($scope, $rootScope, $stateParams, entity, Location, Workorder) {
        $scope.location = entity;
        $scope.load = function (id) {
            Location.get({id: id}, function(result) {
                $scope.location = result;
            });
        };
        $rootScope.$on('thrApp:locationUpdate', function(event, result) {
            $scope.location = result;
        });
    });
