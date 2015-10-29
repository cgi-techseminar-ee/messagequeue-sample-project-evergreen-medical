'use strict';

angular.module('thrApp')
    .controller('EmployeeDetailController', function ($scope, $rootScope, $stateParams, entity, Employee, Workorder) {
        $scope.employee = entity;
        $scope.load = function (id) {
            Employee.get({id: id}, function(result) {
                $scope.employee = result;
            });
        };
        $rootScope.$on('thrApp:employeeUpdate', function(event, result) {
            $scope.employee = result;
        });
    });
