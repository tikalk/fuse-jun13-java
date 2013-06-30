package controllers

import scala.concurrent.Future
import scala.concurrent.duration._
import play.api.libs.iteratee._
import play.api.libs.json._
import akka.actor._
import akka.pattern.ask
import play.api.libs.concurrent._
import akka.util.Timeout

import play.api.Play.current
import play.api.libs.concurrent.Execution.Implicits._

object GameController {
  implicit val timeout = Timeout(1 second)
  def start:Future[(Iteratee[JsValue,_],Enumerator[JsValue])] = {
    val actor = createActor
    (actor ? Start()).map {
      case StartSuccessful(enumerator) =>

        //Create Iteratee to consume the feed
        val iteratee = Iteratee.foreach[JsValue] { event =>
            actor ! ProcessInput(event)          
        }.mapDone {_ =>
          actor ! Quit()
        }

        (iteratee, enumerator)
    }
  }
  
  def createActor = {
    val engineActor = Akka.system.actorOf(Props[GameController])
    //TODO initialize actor
    engineActor
  }
}

class GameController extends Actor {
  implicit val timeout = Timeout(1 second)
  private val (engineEnumerator, engineChannel) = Concurrent.broadcast[JsValue]
  private var StudioSessionData = null

  def receive = {  
      case Start() =>
      sender ! StartSuccessful(engineEnumerator)
  }
}


case class Start()
case class Quit()

case class ProcessInput(input:JsValue)

case class StartSuccessful(enumerator:Enumerator[JsValue])