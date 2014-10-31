angular.module('main').controller('UserDictionary', function ($scope, $http, $cookies, $modal, LanguageFactory) {

    $scope.languages = LanguageFactory.getLanguages();

    $scope.userWords = [{'word': 'word', 'language': {'languageName': 'English'}},
        {'word': 'das Wort', 'language': {'languageName': 'Deutsch'}}];

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
            language: word.language.languageName
        }
    };

    $scope.isBlank = function (s) {
        return s == null || s == "";
    };

    $scope.isNotValid = function () {
        return $scope.word == null || $scope.isBlank($scope.word.word) || $scope.isBlank($scope.word.language);
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

    $scope.$on('language:refresh', function (event, data) {
        $scope.languages = data;
    });

    $scope.refreshWords();
    $scope.sayHi();
});