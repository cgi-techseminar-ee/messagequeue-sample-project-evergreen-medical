'use strict';

angular.module('thrApp').controller('LocationDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Location', 'Workorder',
        function($scope, $stateParams, $modalInstance, entity, Location, Workorder) {

        $scope.location = entity;
        $scope.workorders = Workorder.query();
        $scope.load = function(id) {
            Location.get({id : id}, function(result) {
                $scope.location = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('thrApp:locationUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.location.id != null) {
                Location.update($scope.location, onSaveFinished);
            } else {
                Location.save($scope.location, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
