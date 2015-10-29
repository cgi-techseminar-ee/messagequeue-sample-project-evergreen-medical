'use strict';

angular.module('thrApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


