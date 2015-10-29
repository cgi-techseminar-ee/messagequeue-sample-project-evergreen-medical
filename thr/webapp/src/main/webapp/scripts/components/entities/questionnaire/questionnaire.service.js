'use strict';

angular.module('thrApp')
    .factory('Questionnaire', function ($resource, DateUtils) {
        return $resource('api/questionnaires/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
