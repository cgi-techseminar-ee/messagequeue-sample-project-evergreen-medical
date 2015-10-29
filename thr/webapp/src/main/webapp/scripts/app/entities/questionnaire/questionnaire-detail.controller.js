'use strict';

angular.module('thrApp')
    .controller('QuestionnaireDetailController', function ($scope, $rootScope, $stateParams, entity, Questionnaire, Workorder) {
        $scope.questionnaire = entity;
        $scope.load = function (id) {
            Questionnaire.get({id: id}, function(result) {
                $scope.questionnaire = result;
            });
        };
        $rootScope.$on('thrApp:questionnaireUpdate', function(event, result) {
            $scope.questionnaire = result;
        });
    });
