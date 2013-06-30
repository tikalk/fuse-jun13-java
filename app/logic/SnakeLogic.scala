package logic

import infrastructure._
import akka.snake.game.java.Player
import logic.PointOps._
import scala.collection.JavaConversions
import scala.util.Random

/**
 * Created with IntelliJ IDEA.
 * User: noam
 * Date: 6/30/13
 * Time: 3:12 PM
 * To change this template use File | Settings | File Templates.
 */
object SnakeLogic {
  val wall = new Wall;
  val boardWidth=40
  val boardHeight=20

  def initializeGameData(players : Set[Player]) : GameData = {
    val board = new Board(boardWidth, boardHeight)

    for (x <- 0 until boardWidth; y <- 0 until boardHeight) {
      board.put(new Point(0,y),wall)
      board.put(new Point(x,0),wall)
      board.put(new Point(boardWidth-1,y),wall)
      board.put(new Point(x,boardHeight-1),wall)
    }

    for (player <- players) {
      val snake=new Snake(player)
      player.setSnake(snake)

      var failed=false
      var currentPoint : Point = null
      var direction : Direction = null

      do {
        currentPoint=new Point(Random.nextInt(boardWidth),Random.nextInt(boardHeight))

        direction=Random.nextInt(4) match {
          case 0 => Direction.UP
          case 1 => Direction.DOWN
          case 2 => Direction.LEFT
          case 3 => Direction.RIGHT
        }

        var point=currentPoint

        for (i <- 0 to 5; !failed) {
          if (board.get(point)!=null) {
            failed=true
          }

          point = point + direction
        }
      } while (failed)

      snake.setTail(currentPoint)

      board.put(currentPoint,new SnakeBody(direction))

      currentPoint=currentPoint+direction

      board.put(currentPoint,new SnakeBody(direction))

      currentPoint=currentPoint+direction

      board.put(currentPoint,new SnakeHead(snake))

      snake.setHead(currentPoint)
    }

    var fruitPoint : Point=null

    do {
      fruitPoint=new Point(Random.nextInt(boardWidth),Random.nextInt(boardHeight))
    } while (board.get(fruitPoint)!=null)

    new GameData(board,JavaConversions.setAsJavaSet(players))
  }

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

  def kill(player: Player) = player.setAlive(false)
}
