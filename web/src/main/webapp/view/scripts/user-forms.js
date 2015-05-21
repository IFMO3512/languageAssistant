angular.module('main').controller('UserForms', function ($scope, $http, $location, UserLoginFactory, UserWordFactory,
                                                         UserLanguageFactory) {

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
        $location.path("/userpage");
    };

    $scope.loginAndToMain = function (user) {
        $scope.login(user);
        $location.path("/userpage");

    };

    $scope.logout = function () {
        UserLoginFactory.logout();
        $location.path("/")
    };

    $scope.login = UserLoginFactory.login;

    $scope.home = function () {
        if ($scope.isLogged()) {
            if ($location.path == "/")
                $location.path("/userpage");
        } else {
            $location.path("/");
        }
    };

    $scope.$on('user:login', function (event, data) {
        $scope.loginResult = data;
        $scope.logged = UserLoginFactory.isLogged();
        UserLanguageFactory.refreshLanguage();
        UserWordFactory.refreshWords(UserLanguageFactory.getLanguage());
    });

    $scope.$on('user:register', function (event, data) {
        $scope.registerResult = data;
        $scope.logged = UserLoginFactory.isLogged();
        console.log($scope.logged);
        UserLanguageFactory.refreshLanguage();
        UserWordFactory.refreshWords(UserLanguageFactory.getLanguage());
    });
});