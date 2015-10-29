'use strict';

angular.module('thrApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('workorder', {
                parent: 'entity',
                url: '/workorders',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'thrApp.workorder.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/workorder/workorders.html',
                        controller: 'WorkorderController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('workorder');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('workorder.detail', {
                parent: 'entity',
                url: '/workorder/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'thrApp.workorder.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/workorder/workorder-detail.html',
                        controller: 'WorkorderDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('workorder');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Workorder', function($stateParams, Workorder) {
                        return Workorder.get({id : $stateParams.id});
                    }]
                }
            })
            .state('workorder.new', {
                parent: 'workorder',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/workorder/workorder-dialog.html',
                        controller: 'WorkorderDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    plannedStart: null,
                                    plannedEnd: null,
                                    actualStart: null,
                                    actualEnd: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('workorder', null, { reload: true });
                    }, function() {
                        $state.go('workorder');
                    })
                }]
            })
            .state('workorder.edit', {
                parent: 'workorder',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/workorder/workorder-dialog.html',
                        controller: 'WorkorderDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Workorder', function(Workorder) {
                                return Workorder.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('workorder', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
