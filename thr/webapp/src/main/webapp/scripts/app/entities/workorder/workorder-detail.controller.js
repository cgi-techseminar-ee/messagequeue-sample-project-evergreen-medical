'use strict';

angular.module('thrApp')
    .controller('WorkorderDetailController', function ($scope, $rootScope, $stateParams, entity, Workorder, Location, Employee, Questionnaire) {
        $scope.workorder = entity;
        $scope.load = function (id) {
            Workorder.get({id: id}, function(result) {
                $scope.workorder = result;
            });
        };
        $rootScope.$on('thrApp:workorderUpdate', function(event, result) {
            $scope.workorder = result;
        });
    });
