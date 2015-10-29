'use strict';

angular.module('thrApp')
    .controller('WorkorderController', function ($scope, Workorder) {
        $scope.workorders = [];
        $scope.loadAll = function() {
            Workorder.query(function(result) {
               $scope.workorders = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Workorder.get({id: id}, function(result) {
                $scope.workorder = result;
                $('#deleteWorkorderConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Workorder.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteWorkorderConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.workorder = {
                plannedStart: null,
                plannedEnd: null,
                actualStart: null,
                actualEnd: null,
                id: null
            };
        };
    });
