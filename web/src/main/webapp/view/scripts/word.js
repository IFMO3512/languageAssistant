angular.module('main').controller('Word', function ($scope, $http, $route, $routeParams, hotkeys, $location,
                                                    $modal, LanguageFactory, $cookies) {
    $scope.word = {};
    $scope.word.word = $routeParams.word;
    $scope.word.language = {};
    $scope.word.language.languageName = $routeParams.language;       // TODO encode russian language!!!

    $scope.userLanguage = 'Russian';        // TODO get from user session
    $scope.translation = '';
    $scope.hidden = true;

    $scope.words = [{'word': 'word', 'language': {'languageName': 'English'}},
        {'word': 'das Wort', 'language': {'languageName': 'Deutsch'}}];

    $scope.languages = LanguageFactory.getLanguages();

    $scope.nextId = 0;

    $scope.showTranslation = function () {
        if ($scope.translation != '')
            $scope.hidden = !$scope.hidden;
    };

    hotkeys.bindTo($scope).add({
        combo: 's',
        description: 'Show or hide translation',
        callback: function () {
            $scope.showTranslation();
        }
    });

    hotkeys.bindTo($scope).add({
        combo: 'ы',
        description: 'Show or hide translation',
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

    hotkeys.bindTo($scope).add({
        combo: 'в',
        description: 'Next word',
        callback: function () {
            $scope.next();
        }
    });

    hotkeys.bindTo($scope).add({
        combo: 'x',
        description: 'Delete word',
        callback: function () {
            $scope.removeWord();
        }
    });

    hotkeys.bindTo($scope).add({
        combo: 'ч',
        description: 'Delete word',
        callback: function () {
            $scope.removeWord();
        }
    });

    hotkeys.bindTo($scope).add({
        combo: 'a',
        description: 'Show translations window',
        callback: function () {
            $scope.open($scope.word);
        }
    });

    hotkeys.bindTo($scope).add({
        combo: 'ф',
        description: 'Show translations window',
        callback: function () {
            $scope.open($scope.word);
        }
    });

    $scope.refreshWords = function () {
        if ($cookies.email == null) return;

        $http({method: 'GET', url: 'user/dictionary/getall'}).
            success(function (data) {
                if (data.code == "OK") {
                    $scope.words = data.data;

                    $scope.refreshNextIndex();
                }
            });
    };

    $scope.isHidden = function () {
        return $scope.hidden;
    };

    $scope.next = function () {
        if ($scope.nextId >= $scope.words.length)
            $location.path('/');

        $scope.word = $scope.words[$scope.nextId];
        $scope.hidden = true;
        $scope.translation = '';

        $scope.nextId++;

        $scope.refreshTranslation();
    };

    $scope.isShowDisabled = function () {
        return $scope.translation == '';
    };

    $scope.refreshTranslation = function () {
        $http({
            method: 'GET', url: 'dictionary/get', params: {
                'word': $scope.word.word,
                'wordLanguage': $scope.word.language.languageName,
                'translationLanguage': $scope.userLanguage
            }
        }).
            success(function (data) {
                if (data.code == "OK") {
                    $scope.translation = data.data.word;
                    $scope.hidden = true;
                }
            });
    };

    $scope.open = function (word) {

        var modalInstance = $modal.open({
            templateUrl: 'pages/translations-modal.html',
            controller: 'LanguageController',
            resolve: {
                items: function () {
                    return {
                        languages: $scope.languages,
                        word: word
                    };
                }
            }
        });

        modalInstance.result.then(function () {
            $scope.refreshTranslation();
        });
    };

    $scope.changeLanguage = function (language) {
        $scope.userLanguage = language;

        $scope.refreshTranslation();
    };

    $scope.removeWord = function () {
        $http({method: 'POST', url: 'user/dictionary/remove', data: $scope.cutWord($scope.word)}).
            success(function (data) {
                if (data.code == "OK")
                    $scope.next();
            });
    };

    $scope.cutWord = function (word) {
        return {
            word: word.word,
            language: word.language.languageName
        }
    };

    $scope.refreshNextIndex = function () {
        for (var i = 0; i < $scope.words.length; i++) {
            if ($scope.word.word === $scope.words[i].word &&
                "undefined" !== typeof $scope.words[i].language &&
                $scope.words[i].language.languageName === $scope.word.languageName) {
                $scope.nextId = i + 1;
            }
        }
    };

    $scope.$on('language:refresh', function (event, data) {
        $scope.languages = data;
    });

    $scope.refreshWords();
    $scope.refreshTranslation();
    $scope.refreshNextIndex();
});