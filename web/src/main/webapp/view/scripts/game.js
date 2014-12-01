angular.module('main').controller('Game', function ($scope) {
    $scope.question = 'Find a good translation';

    $scope.quests = [{
        word: 'Word', options: [{word: 'Wort'}, {word: 'Morgen'}, {word: 'Platz'}, {word: 'Spiele'}],
        answer: 0
    }, {
        word: 'Auge', options: [{word: 'Magic'}, {word: 'Morning'}, {word: 'Sun'}, {word: 'Eye'}],
        answer: 3
    }];

    $scope.round = 0;
    $scope.answered = false;
    $scope.progress = 0;

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
    };

    $scope.isAnswered = function () {
        return $scope.answered;
    };

    $scope.next = function () {
        $scope.answered = false;
        $scope.round += 1;
        $scope.setState($scope.round);
    };


    $scope.setState($scope.round);
});