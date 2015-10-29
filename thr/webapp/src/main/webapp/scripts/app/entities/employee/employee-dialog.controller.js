'use strict';

angular.module('thrApp').controller('EmployeeDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Employee', 'Workorder',
        function($scope, $stateParams, $modalInstance, entity, Employee, Workorder) {

        $scope.employee = entity;
        $scope.workorders = Workorder.query();
        $scope.load = function(id) {
            Employee.get({id : id}, function(result) {
                $scope.employee = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('thrApp:employeeUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.employee.id != null) {
                Employee.update($scope.employee, onSaveFinished);
            } else {
                Employee.save($scope.employee, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
