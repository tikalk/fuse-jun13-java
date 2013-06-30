package algorithm

import akka.snake.game.java._

/**
 * User: Nimrod Argov
 * Date: 6/30/13
 * Time: 1:55 PM
 */
object SnakeLogic {

  def init(players: List[Player]): GameData = {
    new GameData()
  }

  def nextStep(prevData: GameData, moves: Map[Player, String]): GameData = {
    new GameData()
  }
}
