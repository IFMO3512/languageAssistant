angular.module('main').controller('Word', function ($scope, $http, $route, $routeParams, hotkeys, $location,
                                                    $modal, LanguageFactory, $cookies, UserWordFactory,
                                                    UserLanguageFactory) {
    $scope.word = {};
    $scope.word.word = $routeParams.word;
    $scope.word.language = {};
    $scope.word.language.languageName = $routeParams.language;       // TODO encode russian language!!!

    $scope.userLanguage = UserLanguageFactory.getLanguage();
    $scope.translation = '';
    $scope.hidden = true;

    $scope.words = UserWordFactory.getWords();

    $scope.languages = LanguageFactory.getLanguages();

    $scope.nextId = 0;

    $scope.score = 0;

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

        UserWordFactory.getWords();
    };

    $scope.isHidden = function () {
        return $scope.hidden;
    };

    $scope.next = function () {
        $scope.score = ($scope.nextId)/$scope.words.length*100;

        if ($scope.nextId >= $scope.words.length) {
            $scope.showResult();
        } else {
            $scope.word = $scope.words[$scope.nextId];
            $scope.hidden = true;

            $scope.nextId++;

            $scope.refreshTranslation();
        }
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
                } else {
                    $scope.translation = '';
                }
            }).error(function (data) {
                $scope.translation = '';
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

        modalInstance.result.then($scope.refreshTranslation, $scope.refreshTranslation);
    };

    $scope.showResult = function () {
        var modalInstance = $modal.open({
            templateUrl: 'pages/word-result.html',
            controller: 'WordResult',
            resolve: {
                items: function () {
                    return {
                        score: $scope.words.length
                    };
                }
            }
        });

        var f = function() {
            $location.path('/profile');
        };

        modalInstance.result.then(f, f);
    };

    $scope.changeLanguage = function (language) {
        $scope.userLanguage = language;

        UserLanguageFactory.refreshLanguage(language);

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
                $scope.words[i].language.languageName === $scope.word.language.languageName) {
                $scope.nextId = i + 1;

                $scope.score = ($scope.nextId-1)/$scope.words.length*100;
            }
        }
    };

    $scope.$on('language:refresh', function (event, data) {
        $scope.languages = data;
    });


    $scope.$on('user:word.refreshed', function () {
        $scope.refreshNextIndex();
    });

    $scope.refreshWords();
    $scope.refreshTranslation();
    $scope.refreshNextIndex();
});