package logic

import infrastructure._
import akka.snake.game.java.Player
import logic.PointOps._

/**
 * Created with IntelliJ IDEA.
 * User: noam
 * Date: 6/30/13
 * Time: 3:12 PM
 * To change this template use File | Settings | File Templates.
 */
class SnakeLogic {
  def kill(player: Player) = player.setAlive(false)

  def movePlayers(gameData : GameData,snakeDirectionChanges : Map[Player,Direction]) : GameData = {
    val board = gameData.getBoard

    for (player : Player <- gameData.getPlayers if player.isAlive) {
      val snake = player.getSnake

      val direction = snakeDirectionChanges.getOrElse(player,snake.getDirection)

      snake.setDirection(direction)

      val headPosition = snake.getHead

      val nextPoint : Point = headPosition + direction

      val boardElement = gameData.getBoard.get(nextPoint)

      boardElement match {
        case Wall | SnakeBody => kill(player)
        case s : SnakeHead => kill(player); kill(s.getSnake.getPlayer)
        case Fruit => {
          val snakeHead=board.get(snake.getHead)
          board.put(snake.getHead,new SnakeBody(snake.getDirection))
          snake.setHead(nextPoint)
          board.put(nextPoint,snakeHead)
        }
        case _ => {
          val snakeHead=board.get(snake.getHead)
          board.put(snake.getHead,new SnakeBody(snake.getDirection))
          snake.setHead(nextPoint)
          board.put(nextPoint,snakeHead)

          val tailDirectionTowardsHead = board.get(snake.getTail) match {
            case sb : SnakeBody => sb.getDirectionTowardsTheHead
          }

          board.remove(snake.getTail)

          snake.setTail(snake.getTail + tailDirectionTowardsHead)
        }
      }
    }

    gameData
  }
}
