'use strict';

angular.module('thrApp').controller('QuestionnaireDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Questionnaire', 'Workorder',
        function($scope, $stateParams, $modalInstance, entity, Questionnaire, Workorder) {

        $scope.questionnaire = entity;
        $scope.workorders = Workorder.query();
        $scope.load = function(id) {
            Questionnaire.get({id : id}, function(result) {
                $scope.questionnaire = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('thrApp:questionnaireUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.questionnaire.id != null) {
                Questionnaire.update($scope.questionnaire, onSaveFinished);
            } else {
                Questionnaire.save($scope.questionnaire, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
