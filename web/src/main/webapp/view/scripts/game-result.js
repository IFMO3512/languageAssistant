angular.module('main').controller('GameResult', function ($scope, $modalInstance, items, hotkeys) {
    $scope.wins = items.wins;
    $scope.loses = items.loses;


    $scope.ok = function () {
        $modalInstance.close();
    };

    hotkeys.bindTo($scope).add({
        combo: 'space',
        description: 'Next',
        callback: function () {
            $scope.ok();
        }
    });
});