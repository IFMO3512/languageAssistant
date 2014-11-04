var app = angular.module('main', ['ui.grid', 'ngRoute', 'ngCookies', 'cfp.hotkeys', 'ui.bootstrap']);

app.config(function ($routeProvider) {
    $routeProvider

        .when('/', {
            templateUrl: 'pages/user.html',
            controller: 'UserDictionary'
        })

        .when('/admin', {
            templateUrl: 'pages/admin.html',
            controller: 'Words'
        })

        .when('/word/:word/:language', {
            templateUrl: 'pages/word.html',
            controller: 'Word'
        });
});


app.factory('LanguageFactory', function LanguageFactory($http, $rootScope) {
    LanguageFactory.languages = [{"languageEnglishName": "Russian", "languageName": "Русский"},
        {"languageEnglishName": "French", "languageName": "Français"},
        {"languageEnglishName": "German", "languageName": "Deutsch"},
        {"languageEnglishName": "Italian", "languageName": "Italiano"}];

    LanguageFactory.refreshLanguages = function () {
        $http({method: 'GET', url: 'languages/getLanguages'}).
            success(function (data) {
                if (data.code == "OK") {
                    LanguageFactory.languages = data.data;
                    console.log("refreshing");
                    $rootScope.$broadcast('language:refresh', data.data);
                }
            });
    };

    LanguageFactory.refreshLanguages();

    return {
        getLanguages: function() {
            return LanguageFactory.languages;
        },
        refreshLanguages: LanguageFactory.refreshLanguages
    };
});

app.factory('UserLanguageFactory', function UserLanguageFactory($http, $rootScope) {

});