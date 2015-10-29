'use strict';

angular.module('thrApp')
    .factory('Workorder', function ($resource, DateUtils) {
        return $resource('api/workorders/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.plannedStart = DateUtils.convertDateTimeFromServer(data.plannedStart);
                    data.plannedEnd = DateUtils.convertDateTimeFromServer(data.plannedEnd);
                    data.actualStart = DateUtils.convertDateTimeFromServer(data.actualStart);
                    data.actualEnd = DateUtils.convertDateTimeFromServer(data.actualEnd);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
