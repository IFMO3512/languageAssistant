angular.module('main').controller('Game', function ($scope, $http, $location, UserWordFactory, $modal, hotkeys) {
    $scope.question = 'Choose correct translation';

    $scope.words = UserWordFactory.getWords();

    $scope.round = 0;
    $scope.answered = false;
    $scope.progress = 0;

    $scope.wins = 0;
    $scope.loses = 0;

    $scope.setState = function (i) {
        var word = $scope.quests[i];
        $scope.word = word.word;

        function extract(n) {
            return word.options[n].word;
        }

        $scope.first = extract(0);
        $scope.second = extract(1);
        $scope.third = extract(2);
        $scope.fourth = extract(3);
        $scope.writeAnswer = word.answer;

        $scope.progress = (i / $scope.quests.length) * 100
    };

    hotkeys.bindTo($scope).add({
        combo: '1',
        description: 'Choose first',
        callback: function () {
            $scope.choose(1);
        }
    });

    hotkeys.bindTo($scope).add({
        combo: '2',
        description: 'Choose second',
        callback: function () {
            $scope.choose(2);
        }
    });

    hotkeys.bindTo($scope).add({
        combo: '3',
        description: 'Choose third',
        callback: function () {
            $scope.choose(3);
        }
    });

    hotkeys.bindTo($scope).add({
        combo: '4',
        description: 'Choose fourth',
        callback: function () {
            $scope.choose(4);
        }
    });

    hotkeys.bindTo($scope).add({
        combo: '4',
        description: 'Choose fourth',
        callback: function () {
            $scope.choose(4);
        }
    });

    hotkeys.bindTo($scope).add({
        combo: 'space',
        description: 'Next',
        callback: function () {
            if($scope.answered)
                $scope.next();
        }
    });

    $scope.style = function (i) {
        if ($scope.answered) {
            if (i == $scope.writeAnswer + 1) {
                return "btn-success";
            }
            else if (i == $scope.choosen) {
                return "btn-danger";
            }
            else {
                return "btn-default";
            }
        } else {
            return "btn-default";
        }
    };

    $scope.choose = function (i) {
        if (!$scope.answered) {
            $scope.answered = true;
            $scope.choosen = i;
        }

        if(i == $scope.writeAnswer + 1)
        {
            $scope.wins = $scope.wins + 1;
        }
        else
        {
            $scope.loses = $scope.loses + 1;
        }
    };

    $scope.isAnswered = function () {
        return $scope.answered;
    };

    $scope.next = function () {
        $scope.answered = false;
        $scope.round += 1;
        if ($scope.round >= $scope.quests.length) {
            $scope.progress = 100;
            $scope.showResult();
        } else
            $scope.setState($scope.round);
    };

    $scope.refreshQuests = function () {
        $http({method: 'GET', url: 'game/quests/'}).
            success(function (data) {
                $scope.quests = data.data;
                $scope.setState($scope.round)
            });
    };

    $scope.showResult = function () {
        var modalInstance = $modal.open({
            templateUrl: 'pages/game-result.html',
            controller: 'GameResult',
            resolve: {
                items: function () {
                    return {
                        wins: $scope.wins,
                        loses: $scope.loses
                    };
                }
            }
        });

        var f = function() {
            $location.path('/profile');
        };

        modalInstance.result.then(f, f);
    };

    $scope.refreshQuests();
});