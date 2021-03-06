angular.module('main').controller('UserDictionary', function ($scope, $http, $modal, UserWordFactory, LanguageFactory,
                                                              UserLoginFactory, UserLanguageFactory) {

    $scope.languages = LanguageFactory.getLanguages();

    $scope.userLanguage = UserLanguageFactory.getLanguage();

    $scope.userWords = UserWordFactory.getWords();

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
        }, function () {
            $scope.refreshWords();
        });
    };

    $scope.changeLanguage = function (language) {
        $scope.userLanguage = language;

        UserLanguageFactory.refreshLanguage(language);

        $scope.refreshWords();
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
        UserWordFactory.refreshWords();
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
        console.log('lang');
        $scope.languages = data;
        $scope.languageName = {};
        $scope.languageName.languageName = "English";
    });

    $scope.$on('user:refreshed', function () {
        console.log('uw');
        $scope.userWords = UserWordFactory.getWords();
    });
});