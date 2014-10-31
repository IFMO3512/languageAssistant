angular.module('main').controller('UserForms', function ($scope, $http, $cookies) {

    $scope.isBlank = function (s) {
        return s == null || s == ""
    };

    $scope.isNotValid = function (user) {
        return user == null || $scope.isBlank(user.email);
    };

    $scope.register = function (user) {
        $http({method: 'POST', url: 'user/add', params: {email: user.email}}).
            success(function (data) {
                if (data.code == "OK") {
                    $scope.registrationResult = "You have been logged in and registered";
                    $cookies.email = user.email;
                    $cookies.name = user.email.split("@")[0];
                    $cookies.domain = user.email.split("@")[1];
                }
                else $scope.registrationResult = "There was an error during registration";
            }).
            error(function () {
                $scope.registrationResult = "Can not connect to the server";
            });
    };

    $scope.login = function (user) {
        $http({method: 'POST', url: 'user/check', params: {email: user.email}}).
            success(function (data) {
                if (data.code == "OK") {
                    $cookies.email = user.email;
                    $cookies.name = user.email.split("@")[0];
                    $cookies.domain = user.email.split("@")[1];
                    $scope.loginResult = "All right";
                } else if (data.code == "NOT_OK") {
                    $scope.loginResult = "Invalid email";
                }
                else $scope.loginResult = "There was an error during registration";
            }).
            error(function () {
                $scope.registrationResult = "Can not connect to the server";
            });
    };
});