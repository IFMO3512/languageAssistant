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
        $http({method: 'GET', url: 'languages/getLanguages'}).
            success(function (data) {
                if (data.code == "OK") {
                    language = data.data();
                    $rootScope.$broadcast('userLanguage:refresh', data.data);
                }
            });
    };

    if ($cookies.domain && $cookies.email && $cookies.name) {   // TODO better check with user factory
        refreshLanguage();
    }

    return {
        getLanguage: function () {
            return language;
        },
        refreshLanguage: refreshLanguage
    }
});

angular.module('main').controller('Game', function ($scope) {
    $scope.question = 'Find a good translation';

    $scope.quests = [{
        word: 'Word', options: [{word: 'Wort'}, {word: 'Morgen'}, {word: 'Platz'}, {word: 'Spiele'}],
        answer: 0
    }, {
        word: 'Auge', options: [{word: 'Magic'}, {word: 'Morning'}, {word: 'Sun'}, {word: 'Eye'}],
        answer: 3
    }];

    $scope.round = 0;
    $scope.answered = false;
    $scope.progress = 0;

    $scope.setState = function(i) {
        var word = $scope.quests[i];
        $scope.word = word.word;

        function extract(n) {
            return word.options[n].word;
        }

        $scope.first = extract(0);
        $scope.second = extract(1);
        $scope.third = extract(2);
        $scope.fourth = extract(3);
        $scope.writeAnswer = word.answer;

        $scope.progress = (i/$scope.quests.length)*100
    };

    $scope.style = function(i) {
        if($scope.answered) {
            if (i == $scope.writeAnswer+1) {
                return "btn-success";
            }
            else if (i == $scope.choosen) {
                return "btn-danger";
            }
            else {
                return "btn-default";
            }
        } else {
            return "btn-default";
        }
    };

    $scope.choose = function(i) {
        if (!$scope.answered) {
            $scope.answered = true;
            $scope.choosen = i;
        }
    };

    $scope.isAnswered = function () {
        return $scope.answered;
    };

    $scope.next = function () {
        $scope.answered = false;
        $scope.round += 1;
        $scope.setState($scope.round);
    };


    $scope.setState($scope.round);
});
// TODO add factory with user factory