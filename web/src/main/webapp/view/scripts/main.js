var app = angular.module('main', ['ui.grid', 'ngRoute', 'ngCookies', 'cfp.hotkeys']);

app.config(function ($routeProvider) {
    $routeProvider

        .when('/', {
            templateUrl: 'pages/user.html',
            controller: 'user-dictionary'
        })

        .when('/admin', {
            templateUrl: 'pages/admin.html',
            controller: 'words'
        })

        .when('/word/:word/:language', {
            templateUrl: 'pages/word.html',
            controller: 'word'
        });
});

app.controller('user-forms', ['$scope', '$http', '$cookies', function ($scope, $http, $cookies) {

    var isBlank = function (s) {
        return s == null || s == ""
    };

    $scope.isNotValid = function (user) {
        return user == null || isBlank(user.email);
    };

    $scope.register = function (user) {
        $http({method: 'POST', url: 'user/add', params: {email: user.email}}).
            success(function (data, status, headers, config) {
                if (data.code == "OK") {
                    $scope.registrationResult = "You have been logged in and registered";
                    $cookies.email = user.email;
                    $cookies.name = user.email.split("@")[0];
                    $cookies.domain = user.email.split("@")[1];
                }
                else $scope.registrationResult = "There was an error during registration";
            }).
            error(function (data, status, headers, config) {
                $scope.registrationResult = "Can not connect to the server";
            });
    };

    $scope.login = function (user) {
        $http({method: 'POST', url: 'user/check', params: {email: user.email}}).
            success(function (data, status, headers, config) {
                if (data.code == "OK") {
                    $cookies.email = user.email;
                    $cookies.name = user.email.split("@")[0];
                    $cookies.domain = user.email.split("@")[1];
                    $scope.loginResult = "All right";
                } else if (data.code == "NOT_OK") {
                    $scope.loginResult = "Invalid email";
                }
                else $scope.loginResult = "There was an error during registration";
            }).
            error(function (data, status, headers, config) {
                $scope.registrationResult = "Can not connect to the server";
            });
    };
}]);

app.controller('user-dictionary', ['$scope', '$http', '$cookies', function ($scope, $http, $cookies) {

    $scope.languages = [{"languageEnglishName":"Russian","languageName":"Русский"},
        {"languageEnglishName":"French","languageName":"Français"},
        {"languageEnglishName":"German","languageName":"Deutsch"},
        {"languageEnglishName":"Italian","languageName":"Italiano"}];

    $scope.userWords = [{'word': 'word','language': {'languageName': 'English'}},
        {'word': 'das Wort','language':{'languageName': 'Deutsch'}}]; 

    var sayHi = function () {
        if ($cookies.email == null) {
            $scope.hi = "Hi, just login, my friend";
            $scope.more = "And here will be fun";
        } else {
            $scope.hi = "Hi, " + $cookies.email;
            $scope.more = "You are logged, so have fun!";
        }
    };

    var cutWord = function (word) {
        return {
            word: word.word,
            language: word.language
        }
    };

    var isBlank = function (s) {
        return s == null || s == "";        // TODO check
    };

    $scope.isNotValid = function () {
        return $scope.word == null || isBlank($scope.word.word) || isBlank($scope.word.language);
    };

    $scope.refreshLanguages = function () {
        $http({method: 'GET', url: 'languages/getLanguages'}).
            success(function (data, status, headers, config) {
                if (data.code == "OK") $scope.languages = data.data;
            });
    };

    $scope.refreshWords = function () {
        $http({method: 'GET', url: 'user/dictionary/getall'}).
            success(function (data, status, headers, config) {
                if (data.code == "OK") $scope.userWords = data.data;
            });
    };

    $scope.addWord = function (word) {
        $http({method: 'POST', url: 'user/dictionary/add', data: word}).
            success(function (data, status, headers, config) {
                $scope.refreshWords();
            });
    };

    $scope.refreshWords();
    $scope.refreshLanguages();
    sayHi();
}]);

app.controller('words', ['$scope', '$http', function ($scope, $http) {

    $scope.languages = [{"languageEnglishName":"Russian","languageName":"Русский"},
        {"languageEnglishName":"French","languageName":"Français"},
        {"languageEnglishName":"German","languageName":"Deutsch"},
        {"languageEnglishName":"Italian","languageName":"Italiano"}];

    $scope.words = [{'word': 'word', 'languageName': 'English'},
        {'word': 'das Wort', 'languageName': 'Deutsch'}
    ];

    $scope.translations = [{word: 'love', languageName: 'English'}];

    var isBlank = function (s) {
        return s == null || s == "";
    };
    // TODO download languages from web but not to delete this languages it will be smoother

    $scope.isNotValid = function () {
        return $scope.source == null || isBlank($scope.source.word) || isBlank($scope.source.language) ||
            $scope.translation == null || isBlank($scope.translation.word) ||
            isBlank($scope.translation.language);
    };

    $scope.addTranslation = function (source, translation) {
        $http({
            method: 'POST', url: 'dictionary/add', data: {
                source: source,
                translation: translation
            }
        })
            .success(function (data, status, headers, config) {
                if (data.code == "OK") {
                    $scope.refreshWords();
                }
            });
    };

    $scope.refreshLanguages = function () {
        $http({method: 'GET', url: 'languages/getLanguages'}).
            success(function (data, status, headers, config) {
                if (data.code == "OK") $scope.languages = data.data;
            });
    };

    $scope.refreshWords = function () {
        $http({method: 'GET', url: 'dictionary/getall'}).
            success(function (data, status, headers, config) {
                if (data.code == "OK") $scope.words = data.data;
            });
    };

    $scope.deleteWord = function (word) {
        $http({method: 'POST', url: 'dictionary/remove', data: cutWord(word)})
            .success(function () {
                $scope.refreshWords();
            });
    };

    $scope.getTranslations = function (word) {
        $http({method: 'POST', url: 'dictionary/translations/get', data: cutWord(word)})
            .success(function (data) {
                $scope.translations = data.data;
            });
    };

    $scope.refreshWords();
    $scope.refreshLanguages();
}]);

app.controller('word', ['$scope', '$http', '$route', '$routeParams', 'hotkeys', '$location',

    function ($scope, $http, $route, $routeParams, hotkeys, $location) {
        $scope.word = {};
        $scope.word.word = $routeParams.word;
        $scope.word.language.languageName = $routeParams.language;

        $scope.userLanguage = 'Russian';        // TODO get from user session
        $scope.translation = '';
        $scope.hiddenTranslation = 'Translation';

        $scope.translations = [                 // TODO there is a problem with language
            {word: 'Patience', languageName: 'English'},
            {word: 'Magic', languageName: 'English'},
            {word: 'Love', languageName: 'English'},
            {word: 'Moments', languageName: 'English'},
            {word: 'Ololo', languageName: 'English'}
        ];

        $scope.languages = [{"languageEnglishName":"Russian","languageName":"Русский"},
            {"languageEnglishName":"French","languageName":"Français"},
            {"languageEnglishName":"German","languageName":"Deutsch"},
            {"languageEnglishName":"Italian","languageName":"Italiano"}];

        $scope.showTranslation = function () {
            if ($scope.translation == '')
                $scope.translation = $scope.hiddenTranslation;
            else
                $scope.translation = '';
        };

        hotkeys.bindTo($scope).add({
            combo: 's',
            description: 'Push down to see Translation',
            callback: function () {
                $scope.showTranslation();
            }
        });

        hotkeys.bindTo($scope).add({
            combo: 'd',
            description: 'Next word',
            callback: function () {
                $scope.next();
            }
        });

        $scope.refreshLanguages = function () {
            $http({method: 'GET', url: 'languages/getLanguages'}).
                success(function (data, status, headers, config) {
                    if (data.code == "OK") $scope.languages = data.data;
                });
        };

        $scope.refreshWords = function () {
            $http({method: 'GET', url: 'user/dictionary/getall'}).
                success(function (data, status, headers, config) {
                    if (data.code == "OK") $scope.translations = data.data;
                });
        };

        $scope.next = function () {
            if ($scope.getNextId() == $scope.translations.length)
                $location.path('/');

            $scope.word = $scope.translations[$scope.getNextId()];
            $routeParams.word = $scope.word.word;
            $scope.hideTranslation = 'translation';

            $scope.refreshTranslation();
        };

        $scope.getNextId = function () {    // TODO can not find the entry ignoring additional parameter
            return $scope.translations.indexOf($scope.word) + 1; // When entry is not found returns 0
        };

        $scope.refreshTranslation = function () {
            $scope.hideTranslation = '';
            $http({
                method: 'GET', url: 'dictionary/get', params: {
                    'word': $scope.word.word,
                    'wordLanguage': $scope.word.language.languageName,
                    'translationLanguage': $scope.userLanguage
                }
            }).
                success(function (data, status, headers, config) {
                    if (data.code == "OK") {
                        $scope.hiddenTranslation = data.data.word;
                        if ($scope.translation != '') $scope.translation = $scope.hiddenTranslation;
                    }
                });
        };

        $scope.changeLanguage = function (language) {
            $scope.userLanguage = language;

            $scope.refreshTranslation();
        };

        $scope.refreshWords();
        $scope.refreshTranslation();
        $scope.refreshLanguages();
    }]);