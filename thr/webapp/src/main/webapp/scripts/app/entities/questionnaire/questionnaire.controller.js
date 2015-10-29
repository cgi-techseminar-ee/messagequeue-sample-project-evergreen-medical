'use strict';

angular.module('thrApp')
    .controller('QuestionnaireController', function ($scope, Questionnaire) {
        $scope.questionnaires = [];
        $scope.loadAll = function() {
            Questionnaire.query(function(result) {
               $scope.questionnaires = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Questionnaire.get({id: id}, function(result) {
                $scope.questionnaire = result;
                $('#deleteQuestionnaireConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Questionnaire.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteQuestionnaireConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.questionnaire = {
                name: null,
                questions: null,
                externalId: null,
                id: null
            };
        };
    });
