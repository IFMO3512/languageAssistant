var app = angular.module('main', ['ui.grid', 'ngRoute', 'ngCookies', 'cfp.hotkeys', 'ui.bootstrap']);

app.config(function ($routeProvider) {
    $routeProvider
        .when('/', {
            templateUrl: 'pages/first.html',
            controller: 'First'
        })

        .when('/profile', {
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
        })

        .when('/game', {
            templateUrl: 'pages/answersGame.html',
            controller: 'Game'
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
        getLanguages: function () {
            return LanguageFactory.languages;
        },
        refreshLanguages: LanguageFactory.refreshLanguages
    };
});

app.factory('UserLanguageFactory', function UserLanguageFactory($http, $rootScope, $cookies) {
    var language = "Russian";

    var refreshLanguage = function () {
        //$http({method: 'GET', url: 'languages/getLanguages'}).
        //    success(function (data) {
        //        if (data.code == "OK") {
        //            language = data.data;
        //            $rootScope.$broadcast('userLanguage:refresh', data.data);
        //        }
        //    });
        // TODO
    };

    if ($cookies.domain && $cookies.email && $cookies.name) {
        refreshLanguage();
    }

    return {
        getLanguage: function () {
            return language;
        },
        refreshLanguage: refreshLanguage
    }
});

app.factory('UserLoginFactory', function UserLoginFactory($http, $rootScope, $cookies, $location) {
    var logged = false;

    var login = function (user) {
        var loginResult;

        $http({method: 'POST', url: 'user/check', params: {email: user.email}}).
            success(function (data) {
                if (data.code == "OK") {
                    $cookies.email = user.email;
                    $cookies.name = user.email.split("@")[0];
                    $cookies.domain = user.email.split("@")[1];
                    loginResult = "All right";

                    logged = true;
                } else if (data.code == "NOT_OK") {
                    loginResult = "Invalid email";
                }
                else loginResult = "There was an error during registration";

                $rootScope.$broadcast('user:login', loginResult);
            }).
            error(function () {
                loginResult = "Can not connect to the server";

                $rootScope.$broadcast('user:login', logged);
            });
    };

    var register = function (user) {
        var registrationResult = "Problem";

        $http({method: 'POST', url: 'user/add', params: {email: user.email}}).
            success(function (data) {
                if (data.code == "OK") {
                    registrationResult = "You have been logged in and registered";
                    $cookies.email = user.email;
                    $cookies.name = user.email.split("@")[0];
                    $cookies.domain = user.email.split("@")[1];

                    logged = true;
                }
                else registrationResult = "There was an error during registration";

                $rootScope.$broadcast('user:register', registrationResult);
            }).
            error(function () {
                registrationResult = "Can not connect to the server";

                $rootScope.$broadcast('user:register', registrationResult);
            });
    };

    var logout = function () {
        logged = false;
        delete $cookies.email;
        delete $cookies.name;
        delete $cookies.domain;
    };

    if ($cookies.domain && $cookies.email && $cookies.name) {
        logged = true;

        $location.path("/");
    }

    return {
        login: login,
        register: register,
        isLogged: function () {
            return logged;
        },
        logout: logout
    }
});


app.factory('UserWordFactory', function ($rootScope, $http, UserLanguageFactory, UserLoginFactory) {
    var words;

    var refreshWords = function (param) {
        if (!param) param = UserLanguageFactory.getLanguage();

        if (!UserLoginFactory.isLogged()) return;

        $http({
            method: 'GET', url: 'user/dictionary/getall/translation', params: {
                language: UserLanguageFactory.getLanguage()
            }
        }).
            success(function (data) {
                if (data.code == "OK") words = data.data;

                $rootScope.$broadcast('user:word.refreshed', words);
            });
    };

    refreshWords(UserLanguageFactory.getLanguage());

    return {
        getWords: function () {
            return words;
        },
        refreshWords: refreshWords
    }
});
