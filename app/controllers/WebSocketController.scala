package controllers

import play.api.mvc._
import play.api.libs.streams.ActorFlow

import javax.inject.Inject
import akka.actor.ActorSystem
import akka.stream.Materializer
import akka.stream.scaladsl._

import scala.concurrent.duration.{Duration, DurationInt}

class WebSocketController @Inject()(cc: ControllerComponents)(implicit system: ActorSystem, mat: Materializer)
  extends AbstractController(cc) {

  def socket():WebSocket = WebSocket.accept[String, String] { request =>

    /**
     * To handle a WebSocket with an actor, we can use a Play utility, ActorFlow to convert an ActorRef to a flow.
     * This utility takes a function that converts the ActorRef to send messages to a akka.actor.Props object
     * that describes the actor that Play should create when it receives the WebSocket connection:
     */
    ActorFlow.actorRef { out =>
      MyWebSocketActor.props(out)
    }
  }

  def akkaStreamsSocket() : WebSocket = WebSocket.accept[String,String]{
      request =>
        // Log events to the console
        val in = Sink.foreach[String](println)
        val out= Source.tick(
          2.seconds, 2.seconds,"Hello from Akka Stream"
        )
        Flow.fromSinkAndSource(in, out)
    }
}

import akka.actor._

object MyWebSocketActor {
  def props(out: ActorRef) = Props(new MyWebSocketActor(out))
}

class MyWebSocketActor(out: ActorRef) extends Actor {
  def receive = {
    case msg: String =>
      out ! ("I received your message: " + msg)
  }
}
