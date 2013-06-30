package controllers

import scala.concurrent.Future
import scala.concurrent.duration._
import play.api.libs.iteratee._
import play.api.libs.json._
import akka.actor._
import akka.pattern.ask
import play.api.libs.concurrent._
import akka.util.Timeout

import akka.snake.game.java._

import play.api.Play.current
import play.api.libs.concurrent.Execution.Implicits._

object GameController {
  implicit val timeout = Timeout(1 second)
  val sl: akka.snake.game.java.SnakeLogic = null
  private val (enumerator, channel) = Concurrent.broadcast[JsValue]

  def start: (Iteratee[JsValue, _], Enumerator[JsValue]) = {
    //    val actor = createActor
    //    (actor ? Start()).map {
    //      case StartSuccessful(enumerator) =>

    //Create Iteratee to consume the feed
    var playerName: String = null
    val snakeApi = Snake.registerUICallback(new SnakeCallback {
      def handleData(gameData: GameData) {
        Console.out.print(".")
      }
    })

    val iteratee = Iteratee.foreach[JsValue] {
      input =>
      //            actor ! ProcessInput(event)
        playerName = (input \ "player").as[String]
        (input \ "action").as[String] match {
          case "join" =>
            snakeApi.register(playerName, "aaa@ppp.com")
          case "start" =>
            snakeApi.startGame()
          case "left" =>
            snakeApi.move(playerName, "left")
          case "right" =>
            snakeApi.move(playerName, "right")
          case "up" =>
            snakeApi.move(playerName, "up")
          case "down" =>
            snakeApi.move(playerName, "down")
        }
    }.mapDone {
      _ =>
      //          actor ! Quit()
        snakeApi.unregister(playerName)
    }

    (iteratee, enumerator)

  }

  /*
  def createActor = {
    val engineActor = Akka.system.actorOf(Props[GameController])
    engineActor
  }  */
}

/*
class GameController extends Actor {
  implicit val timeout = Timeout(1 second)
  private val (engineEnumerator, engineChannel) = Concurrent.broadcast[JsValue]
  
  private var snakeApi:SnakeApi = null
  private var playerName:String = null

  def receive = {  
      case Start() =>
        snakeApi = Snake.registerUICallback(new SnakeCallback {
          def handleData(gameData:GameData) {
            
          }
        })
        
        sender ! StartSuccessful(engineEnumerator)
      case ProcessInput(input:JsValue) =>
        playerName = (input \ "player").as[String]
        (input \ "action").as[String] match {
          case "join" =>
            snakeApi.register(playerName, "aaa@ppp.com")
          case "start" =>
            snakeApi.startGame()
          case "left" =>
            snakeApi.move(playerName, "left")
          case "right" =>
            snakeApi.move(playerName, "right")
          case "up" =>
            snakeApi.move(playerName, "up")
          case "down" =>
            snakeApi.move(playerName, "down")
        }
      case Quit() =>
        snakeApi.unregister(playerName)
  }
}
*/

case class Start()

case class Quit()

case class ProcessInput(input: JsValue)

case class StartSuccessful(enumerator: Enumerator[JsValue])