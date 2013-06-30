package logic

import akka.snake.game.java.{Snake => WhoTheFuckNeedsThisClass, GameData => _, _}
import infrastructure._

/**
 * Created with IntelliJ IDEA.
 * User: noam
 * Date: 6/30/13
 * Time: 2:37 PM
 * To change this template use File | Settings | File Templates.
 */
class BoardInitializer {
  val wall = new Wall;

  def initializeBoard(players: List[Player]): GameData = {
    val board = new Board(10, 10)
                      `
    for (x <- 0 to 9;
         y <- 0 to 9) {
        board.put(new Point(0,y),wall)
        board.put(new Point(x,0),wall)
        board.put(new Point(9,y),wall)
        board.put(new Point(x,9),wall)
    }

    for (player <- players) {
      val snake=new infrastructure.Snake()



      player.setSnake(snake);
    }


    //fruits
    val gameData = new GameData(board,players,null)

  }
}
