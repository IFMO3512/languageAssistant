angular.module('main').controller('Words', function ($scope, $http, $modal, LanguageFactory) {

    $scope.languages = LanguageFactory.getLanguages();

    $scope.language = 'Russian';

    $scope.words = [{
        source: {
            word: "Магия",
            language: {"languageEnglishName": "Russian", "languageName": "Русский"}
        },
        translation: {"word": "Magic", "language": {"languageEnglishName": "English", "languageName": "English"}}
    }, {
        source: {"word": "Magic", "language": {"languageEnglishName": "English", "languageName": "English"}},
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