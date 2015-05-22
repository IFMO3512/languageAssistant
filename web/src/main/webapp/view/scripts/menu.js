angular.module('main').controller('Menu', function ($scope, UserWordFactory, UserLanguageFactory) {

    $scope.userWords = UserWordFactory.getWords();

    $scope.first = 0;

    $scope.showCards = function () {
        $scope.word = $scope.userWords[$scope.first];

        $scope.userLanguage = UserLanguageFactory.getLanguage();


    };

});
