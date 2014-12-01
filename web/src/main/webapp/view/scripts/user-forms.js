angular.module('main').controller('UserForms', function ($scope, $http, $location, UserLoginFactory) {

    $scope.isBlank = function (s) {
        return s == null || s == ""
    };

    $scope.isLogged = UserLoginFactory.isLogged;

    $scope.isNotValid = function (user) {
        return user == null || $scope.isBlank(user.email);
    };

    $scope.register = UserLoginFactory.register;

    $scope.registerAndToMain = function (user) {
        $scope.register(user);
        $location.path("/profile");
    };

    $scope.loginAndToMain = function (user) {
        $scope.login(user);
        $location.path("/profile");

    };

    $scope.logout = function () {
        UserLoginFactory.logout();
        $location.path("/")
    };

    $scope.login = UserLoginFactory.login;

    $scope.home = function () {
        if ($scope.isLogged()) {
            $location.path("/profile");
        } else {
            $location.path("/");
        }
    };

    $scope.$on('user:login', function (event, data) {
        $scope.loginResult = data;
        $scope.logged = UserLoginFactory.isLogged();
    });

    $scope.$on('user:register', function (event, data) {
        $scope.registerResult = data;
        $scope.logged = UserLoginFactory.isLogged();
    });
});