angular.module('main').controller('WordResult', function ($scope, $modalInstance, items, hotkeys) {
    $scope.score = items.score;

    $scope.ok = function () {
        $modalInstance.close();
    };

    hotkeys.bindTo($scope).add({
        combo: 'd',
        description: 'Ok',
        callback: function () {
            $scope.ok();
        }
    });

    hotkeys.bindTo($scope).add({
        combo: 'в',
        description: 'Ok',
        callback: function () {
            $scope.ok();
        }
    });
});