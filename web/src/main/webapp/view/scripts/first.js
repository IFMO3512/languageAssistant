angular.module('main').controller('First', function ($scope, $location, UserLoginFactory) {
    if (UserLoginFactory.isLogged()) {
        $location.path("/profile");
    }
});