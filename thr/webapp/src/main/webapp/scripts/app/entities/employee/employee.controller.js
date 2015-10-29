'use strict';

angular.module('thrApp')
    .controller('EmployeeController', function ($scope, Employee) {
        $scope.employees = [];
        $scope.loadAll = function() {
            Employee.query(function(result) {
               $scope.employees = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Employee.get({id: id}, function(result) {
                $scope.employee = result;
                $('#deleteEmployeeConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Employee.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteEmployeeConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.employee = {
                name: null,
                externalId: null,
                id: null
            };
        };
    });
