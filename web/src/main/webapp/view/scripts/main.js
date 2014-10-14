var app = angular.module('main', ['ui.grid', 'ngRoute', 'ngCookies']);

app.config(function($routeProvider) {
    $routeProvider

        .when('/', {
            templateUrl : 'pages/login.html',
            controller  : 'registrationForm'
        })

        .when('/admin', {
            templateUrl : 'pages/admin.html',
            controller  : 'words'
        });
});

app.controller('registrationForm', ['$scope', '$http', '$cookies', function ($scope, $http, $cookies) {
    $scope.isNotValid = function (user) {
        return user == null || isBlank(user.email);
    };

    var isBlank = function (s) {
        return s == null || s == ""
    };

    $scope.register = function (user) {
        $http({method: 'POST', url: 'user/add', params: {email: user.email}}).
            success(function (data, status, headers, config) {
                if (data.code == "OK") $scope.registrationResult = "You have been registered";
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
                    $scope.loginResult = "All right";
                } else if(data.code == "NOT_OK") {
                    $scope.loginResult = "Invalid email";
                }
                else $scope.loginResult = "There was an error during registration";
            }).
            error(function (data, status, headers, config) {
                $scope.registrationResult = "Can not connect to the server";
            });
    };
}]);

app.controller('words', ['$scope', '$http', function ($scope, $http) {
    $scope.languages = ['English', 'Russian', 'Spanish', 'Italian'];
    // TODO download languages from web but not to delete this languages it will be smoother

    $scope.isNotValid = function () {
        return $scope.source == null || isBlank($scope.source.word) || isBlank($scope.source.language) ||
            $scope.translation == null || isBlank($scope.translation.word) ||
            isBlank($scope.translation.language);
    };

    var isBlank = function (s) {
        return s == null || s == "";
    };

    $scope.words = [
        {'word': 'word', 'languageName': 'English'},
        {'word': 'das Wort', 'languageName': 'Deutsch'},
        {'word': 'слово', 'languageName': 'Русский'}
    ];

    $scope.addTranslation = function (source, translation) {
        $http({method: 'POST', url: 'dictionary/add', data: {source: $scope.source,
            translation: $scope.translation}}).
            success(function (data, status, headers, config) {
                if (data.code == "OK") {
                    $scope.registrationResult = "You have been registered";
                }
                else $scope.registrationResult = "There was an error during registration";
                $scope.refreshWords();
            }).
            error(function (data, status, headers, config) {
                $scope.registrationResult = "Can not connect to the server";
            });
    };

    $scope.refreshWords = function () {
        $http({method: 'GET', url: 'dictionary/getall'}).
            success(function (data, status, headers, config) {
                if (data.code == "OK") $scope.words = data.data;
            }).
            error(function (data, status, headers, config) {

            });
    };

    $scope.refreshWords();

    $scope.delete = function (word) {
        $http({method: 'POST', url: 'dictionary/remove', data: cutWord(word)})
            .success(function () {
                $scope.refreshWords();
            });
    };

    cutWord = function (word) {
        return {
            word: word.word,
            language: word.language
        }
    };

    $scope.translations = [
        {word: 'love', languageName: 'English'}
    ];

    $scope.getTranslations = function (word) {
        $http({method: 'POST', url: 'dictionary/translations/get', data: cutWord(word)})
            .success(function (data) {
                $scope.translations = data.data;
            });
    };
}]);