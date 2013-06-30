package logic

import infrastructure.{Direction, GameData, Board, Snake}
import akka.snake.game.java.Player

/**
 * Created with IntelliJ IDEA.
 * User: noam
 * Date: 6/30/13
 * Time: 3:12 PM
 * To change this template use File | Settings | File Templates.
 */
class SnakeLogic {
  def advanceSnake(gameData : GameData,snakeDirectionChanges : Map[Player,Direction]) : GameData = {
    for (player : Player <- gameData.getPlayers) {
      val snake = player.getSnake

      val direction : Direction = snakeDirectionChanges.getOrElse(player,snake.getDirection)

      snake.setDirection(direction)

      val headPosition = snake.getHead







    }



    gameData
  }
}
