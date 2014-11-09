angular.module('main').controller('LanguageController', function($scope, $http, $modalInstance, items) {
    // TODO add autofocus on load
    $scope.languages = items.languages;

    $scope.word = items.word;

    $scope.translations = [{word: 'love', language: {languageName: 'English'}}];

    $scope.isNewTranslationInvalid = function () {
        return $scope.newTranslation == null || $scope.isBlank($scope.newTranslation.word) ||
            $scope.newTranslation.language == null || $scope.isBlank($scope.newTranslation.language);
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

    $scope.addTranslation = function (word, translation) {
        $http({
            method: 'POST', url: 'dictionary/add', data: $scope.getWordWithTranslation(word, translation)
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
            language: word.language.languageName
        }
    };

    $scope.getWordWithTranslation = function(word, translation) {
        return {
            word: word.word,
            language: word.language.languageName,
            translation: {
                word : translation.word,
                language: translation.language
            }
        }
    };

    $scope.refreshTranslations($scope.word);
});