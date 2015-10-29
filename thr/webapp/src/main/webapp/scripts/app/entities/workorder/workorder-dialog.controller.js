'use strict';

angular.module('thrApp').controller('WorkorderDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Workorder', 'Location', 'Employee', 'Questionnaire',
        function($scope, $stateParams, $modalInstance, entity, Workorder, Location, Employee, Questionnaire) {

        $scope.workorder = entity;
        $scope.locations = Location.query();
        $scope.employees = Employee.query();
        $scope.questionnaires = Questionnaire.query();
        $scope.load = function(id) {
            Workorder.get({id : id}, function(result) {
                $scope.workorder = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('thrApp:workorderUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.workorder.id != null) {
                Workorder.update($scope.workorder, onSaveFinished);
            } else {
                Workorder.save($scope.workorder, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
