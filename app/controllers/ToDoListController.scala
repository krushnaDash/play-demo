package controllers

import akka.http.scaladsl.model.HttpHeader.ParsingResult.Ok
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}
import play.api.libs.json._

import javax.inject.{Inject, Singleton}
import scala.::
import scala.concurrent.{ExecutionContext, Future}

case class WorkList(id: Long, description: String, var isItDone: Boolean)

case class NewWorkList(description: String)

@Singleton
class ToDoListController @Inject()(val controllerComponents: ControllerComponents)(implicit ec: ExecutionContext) extends BaseController {
  // Json.toJson need an implicit parameter of Json formater
  implicit val workJson = Json.format[WorkList]

  var toDoList = List(WorkList(1, "Complete Akka", false), WorkList(2, "Complete Spark", false), WorkList(3, "Complete Kafka", true))

  def getAll(): Action[AnyContent] = Action {
    Ok(Json.toJson(toDoList))
  }

  def getById(id: Long): Action[AnyContent] = Action {
    toDoList.find(_.id == id) match {
      case Some(value) => Ok(Json.toJson(value))
      case None => NotFound
    }
  }

  def markAsDone(id: Long): Action[AnyContent] = Action {
    toDoList.find(_.id == id) match {
      case Some(value) =>
        value.copy(isItDone = true)
        value.isItDone = true
        Ok(Json.toJson(value))
      case None => NotFound
    }
  }

  def addNewItem(): Action[AnyContent] = Action {
    implicit request =>
      val content = request.body
      val jsonObject = content.asJson
      implicit val newWorkJson = Json.format[NewWorkList]
      val newObject = jsonObject.flatMap(Json.fromJson[NewWorkList](_).asOpt)
      newObject match {
        case Some(value) =>
          val nextId = toDoList.map(_.id).max + 1
          val toBeadded = WorkList(nextId, value.description, false)
          toDoList ::= toBeadded
          Created(Json.toJson(toBeadded)(workJson))
        case None => BadRequest
      }
  }

  // Async example
  def getAllByAsync(): Action[AnyContent] = Action.async {
    implicit request =>
      Future(Ok(Json.toJson(toDoList)))
  }
}
