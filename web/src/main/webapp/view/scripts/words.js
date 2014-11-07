angular.module('main').controller('Words', function ($scope, $http, $modal, LanguageFactory, UserLanguageFactory) {

    $scope.languages = LanguageFactory.getLanguages();

    $scope.language = 'Russian';

    $scope.userWords = [{
        word: "Love",
        language: {"languageEnglishName": "English", "languageName": "English"},
        translation: {"word": "Magic", "language": {"languageEnglishName": "English", "languageName": "English"}}
    }, {
        "word": "Magic",
        "language": {"languageEnglishName": "English", "languageName": "English"},
        translation: null
    }];


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
        return $scope.word == null || $scope.isBlank($scope.word.word) || $scope.isBlank($scope.word.language) ||
            $scope.word.translation == null || $scope.isBlank($scope.word.translation.word) ||
            $scope.isBlank($scope.word.translation.language);
    };

    $scope.addTranslation = function (word) {
        $http({
            method: 'POST', url: 'dictionary/add', data: word
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
        $http({method: 'GET', url: 'dictionary/translations/getall', params: {language: $scope.language}}).
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
            language: word.language.languageName
        }
    };

    $scope.$on('language:refresh', function (event, data) {
        $scope.languages = data;
    });

    $scope.refreshWords();
});