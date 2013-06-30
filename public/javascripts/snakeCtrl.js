angular.module('snakes', []);

function SnakesCtrl($scope) {
    $scope.gameData={

        board:[
            // n    - player n
            // -n   - head of player n
            // null - empty
            // "A"  - apple
            // "B"  - block
            [ "B", "B", "B", "B", "B", "B", "B", "B", "B", "B"],
            [ "B",null,null,null,null,null,null,null,null, "B"],
            [ "B",null,null,null,null,null,null,null,null, "B"],
            [ "B",null,null,null,null,null,null,null,null, "B"],
            [ "B",null,null,null,null,null,   1,  -1,null, "B"],
            [ "B",null,null,null,null,null,   1,null,null, "B"],
            [null,null,null,null,null,null,null,   0,null,null],
            [ "B",null,null,null,null,null,null,null,null, "B"],
            [ "B",  -2,   2,   2,   2,null,null,null,null, "B"],
            [ "B",null,null,null,null,null,null,null,null, "B"],
            [ "B",null,null,null,null,null,null,null,null, "B"],
            [ "B", "B", "B", "B", "B", "B", "B", "B", "B", "B"]
        ],

        players:{
            "player1":"n",
            "player2":"s"
        },

        currentPlayer:"player1",

        state:"waiting", //"waiting", "playing", "gameOver"

        score:{
            "player1":100,
            "player2":200
        }
    }

    var snakesWebSocket = null;

    $scope.onInit = function (wsUrl) {
        var WS = window['MozWebSocket'] ? MozWebSocket : WebSocket
        snakesWebSocket = new WS (wsUrl)

        snakesWebSocket.onmessage = onWSMessage

        $(document.body).on('keydown', onkeydown);

    }

    var onWSMessage = function (message) {
        var newData = JSON.parse(message.data)
        $scope.gameData = newData
    }

    var onkeydown = function(e) {
        var toSend = null;
        switch (e.which) {
            // key code for left arrow
            case 37:
                toSend = JSON.stringify({action:"left", player:$scope.gameData.currentPlayer})
                break;
            case 38:
                toSend = JSON.stringify({action:"up", player:$scope.gameData.currentPlayer})
                break;
            // key code for right arrow
            case 39:
                toSend = JSON.stringify({action:"right", player:$scope.gameData.currentPlayer})
                break;
            case 40:
                toSend = JSON.stringify({action:"down", player:$scope.gameData.currentPlayer})
                break;
        }
        snakesWebSocket.send(toSend)
    }
    $scope.onJoinClick = function() {
        var toSend = JSON.stringify({action:"join", player:$scope.gameData.currentPlayer})
        snakesWebSocket.send(toSend)
    }
    
    $scope.onStartClick = function() {
        var toSend = JSON.stringify({action:"start", player:$scope.gameData.currentPlayer})
        snakesWebSocket.send(toSend)
    }
}


