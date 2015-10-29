'use strict';

angular.module('thrApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('questionnaire', {
                parent: 'entity',
                url: '/questionnaires',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'thrApp.questionnaire.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/questionnaire/questionnaires.html',
                        controller: 'QuestionnaireController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('questionnaire');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('questionnaire.detail', {
                parent: 'entity',
                url: '/questionnaire/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'thrApp.questionnaire.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/questionnaire/questionnaire-detail.html',
                        controller: 'QuestionnaireDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('questionnaire');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Questionnaire', function($stateParams, Questionnaire) {
                        return Questionnaire.get({id : $stateParams.id});
                    }]
                }
            })
            .state('questionnaire.new', {
                parent: 'questionnaire',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/questionnaire/questionnaire-dialog.html',
                        controller: 'QuestionnaireDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    questions: null,
                                    externalId: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('questionnaire', null, { reload: true });
                    }, function() {
                        $state.go('questionnaire');
                    })
                }]
            })
            .state('questionnaire.edit', {
                parent: 'questionnaire',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/questionnaire/questionnaire-dialog.html',
                        controller: 'QuestionnaireDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Questionnaire', function(Questionnaire) {
                                return Questionnaire.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('questionnaire', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
