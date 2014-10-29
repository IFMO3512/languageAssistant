var app = angular.module('main', ['ui.grid', 'ngRoute', 'ngCookies', 'cfp.hotkeys', 'ui.bootstrap']);

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

    $scope.isBlank = function (s) {
        return s == null || s == ""
    };

    $scope.isNotValid = function (user) {
        return user == null || $scope.isBlank(user.email);
    };

    $scope.register = function (user) {
        $http({method: 'POST', url: 'user/add', params: {email: user.email}}).
            success(function (data) {
                if (data.code == "OK") {
                    $scope.registrationResult = "You have been logged in and registered";
                    $cookies.email = user.email;
                    $cookies.name = user.email.split("@")[0];
                    $cookies.domain = user.email.split("@")[1];
                }
                else $scope.registrationResult = "There was an error during registration";
            }).
            error(function () {
                $scope.registrationResult = "Can not connect to the server";
            });
    };

    $scope.login = function (user) {
        $http({method: 'POST', url: 'user/check', params: {email: user.email}}).
            success(function (data) {
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
            error(function () {
                $scope.registrationResult = "Can not connect to the server";
            });
    };
}]);

app.controller('user-dictionary', ['$scope', '$http', '$cookies', '$modal', function ($scope, $http, $cookies, $modal) {

    $scope.languages = [{"languageEnglishName":"Russian","languageName":"Русский"},
                        {"languageEnglishName":"French","languageName":"Français"},
                        {"languageEnglishName":"German","languageName":"Deutsch"},
                        {"languageEnglishName":"Italian","languageName":"Italiano"}];

    $scope.userWords = [{'word': 'word', 'languageName': 'English'},
                        {'word': 'das Wort', 'languageName': 'Deutsch'}];

    $scope.sayHi = function () {
        if ($cookies.email == null) {
            $scope.hi = "Hi, just login, my friend";
            $scope.more = "And here will be fun";
        } else {
            $scope.hi = "Hi, " + $cookies.email;
            $scope.more = "You are logged, so have fun!";
        }
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
            $scope.refreshWords();
        });
    };

    $scope.cutWord = function (word) {
        return {
            word: word.word,
            language: word.languageName
        }
    };

    $scope.isBlank = function (s) {
        return s == null || s == "";
    };

    $scope.isNotValid = function () {
        return $scope.word == null || $scope.isBlank($scope.word.word) || $scope.isBlank($scope.word.language);
    };

    $scope.refreshLanguages = function () {
        $http({method: 'GET', url: 'languages/getLanguages'}).
            success(function (data) {
                if (data.code == "OK") $scope.languages = data.data;
            });
    };

    $scope.refreshWords = function () {
        $http({method: 'GET', url: 'user/dictionary/getall'}).
            success(function (data) {
                if (data.code == "OK") $scope.userWords = data.data;
            });
    };

    $scope.addWord = function (word) {
        $http({method: 'POST', url: 'user/dictionary/add', data: word}).
            success(function () {
                $scope.refreshWords();
            });
    };

    $scope.removeWord = function (word) {
        $http({method: 'POST', url: 'user/dictionary/remove', data: $scope.cutWord(word)}).
            success(function (data) {
                if (data.code == "OK")
                    $scope.refreshWords();
            });
    };

    $scope.refreshWords();
    $scope.refreshLanguages();
    $scope.sayHi();
}]);

app.controller('words', ['$scope', '$http', '$modal', function ($scope, $http, $modal) {
  
    $scope.languages = [{"languageEnglishName":"Russian","languageName":"Русский"},
        {"languageEnglishName":"French","languageName":"Français"},
        {"languageEnglishName":"German","languageName":"Deutsch"},
        {"languageEnglishName":"Italian","languageName":"Italiano"}];

    $scope.words = [{'word': 'word', 'languageName': 'English'},
        {'word': 'das Wort', 'languageName': 'Deutsch'}
    ];

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
            $scope.refreshWords();
        });
    };

    $scope.isBlank = function (s) {
        return s == null || s == "";
    };

    $scope.isNotValid = function () {
        return $scope.source == null || $scope.isBlank($scope.source.word) || $scope.isBlank($scope.source.language) ||
            $scope.translation == null || $scope.isBlank($scope.translation.word) ||
            $scope.isBlank($scope.translation.language);
    };

    $scope.addTranslation = function (source, translation) {
        $http({
            method: 'POST', url: 'dictionary/add', data: {
                source: source,
                translation: translation
            }
        })
            .success(function (data) {
                if (data.code == "OK") {
                    $scope.refreshWords();
                }
            });
    };

    $scope.refreshLanguages = function () {
        $http({method: 'GET', url: 'languages/getLanguages'}).
            success(function (data) {
                if (data.code == "OK") $scope.languages = data.data;
            });
    };

    $scope.refreshWords = function () {
        $http({method: 'GET', url: 'dictionary/getall'}).
            success(function (data) {
                if (data.code == "OK") $scope.words = data.data;
            });
    };

    $scope.deleteWord = function (word) {
        $http({method: 'POST', url: 'dictionary/remove', data: $scope.cutWord(word)})
            .success(function () {
                $scope.refreshWords();
            });
    };

    $scope.cutWord = function (word) {
        return {
            word: word.word,
            language: word.languageName
        }
    };

    $scope.refreshWords();
    $scope.refreshLanguages();
}]);

app.controller('word', ['$scope', '$http', '$route', '$routeParams', 'hotkeys', '$location', '$modal',
    function ($scope, $http, $route, $routeParams, hotkeys, $location, $modal) {
        $scope.word = {};
        $scope.word.word = $routeParams.word;
        $scope.word.languageName = $routeParams.language;       // TODO encode russian language!!!

        $scope.userLanguage = 'Russian';
        $scope.translation = '';
        $scope.hidden = true;

        $scope.words = [
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

        $scope.refreshLanguages = function () {
            $http({method: 'GET', url: 'languages/getLanguages'}).
                success(function (data) {
                    if (data.code == "OK") $scope.languages = data.data;
                });
        };

        $scope.refreshWords = function () {
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

        $scope.isShowDisabled = function() {
            return $scope.translation == '';
        };

        $scope.refreshTranslation = function () {
            $http({
                method: 'GET', url: 'dictionary/get', params: {
                    'word': $scope.word.word,
                    'wordLanguage': $scope.word.languageName,
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
                language: word.languageName
            }
        };

        $scope.refreshNextIndex = function() {
            for (var i = 0; i < $scope.words.length; i++) {
                if ($scope.word.word === $scope.words[i].word &&
                    "undefined" !== typeof $scope.words[i].language &&
                    $scope.words[i].language.languageName === $scope.word.languageName) {
                    $scope.nextId = i+1;
                }
            }
        };

        $scope.refreshWords();
        $scope.refreshTranslation();
        $scope.refreshLanguages();
        $scope.refreshNextIndex();
    }]);


app.controller('LanguageController', function($scope, $http, $modalInstance, items) {
    // TODO add autofocus on load
    $scope.languages = items.languages;

    $scope.word = items.word;

    $scope.translations = [{word: 'love', languageName: 'English'}];

    $scope.isNewTranslationInvalid = function () {
        return $scope.newTranslation == null || $scope.isBlank($scope.newTranslation.word) ||
            $scope.isBlank($scope.newTranslation.language);
    };

    $scope.isBlank = function (s) {
        return s == null || s == "";
    };

    $scope.refreshTranslations = function (word) {
        $scope.newTranslation = {};
        $http({method: 'POST', url: 'dictionary/translations/get', data: $scope.cutWord(word)})
            .success(function (data) {
                $scope.translations = data.data;
            });
    };

    $scope.addTranslation = function (source, translation) {
        $http({
            method: 'POST', url: 'dictionary/add', data: {
                source: source,
                translation: translation
            }
        })
            .success(function (data) {
                if (data.code == "OK") {
                    $scope.refreshTranslations($scope.word);
                }
            });
    };

    $scope.cutWord = function (word) {
        return {
            word: word.word,
            language: word.languageName
        }
    };

    $scope.refreshTranslations($scope.word);
});