package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import repositories.AnimalRepository
import models.Animal
import models.UniqueId
import java.time.LocalDateTime
import play.api.libs.json.Json
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.util.Success
import scala.util.Failure
import play.api.libs.json.JsValue

@Singleton
class AnimalController @Inject() (
    val controllerComponents: ControllerComponents,
    animalRepository: AnimalRepository
)(implicit ec: ExecutionContext)
    extends BaseController {

  def create() = Action.async { implicit request: Request[AnyContent] =>
    val reqBody: Option[JsValue] = request.body.asJson
    val success = animalRepository.insert(
      new Animal(
        UniqueId(),
        (reqBody.get("nickname")).as[String],
        (reqBody.get("animalType")).as[String],
        LocalDateTime.now(),
        LocalDateTime.now()
      )
    )

    success.map { wasSuccessful =>
      Ok(wasSuccessful.toString())
    }
  }

  def index() = Action.async { implicit request: Request[AnyContent] =>
    val results = animalRepository.findAll()

    /*
    .map when used on a future is different than an iterable !!

    .map Creates a new future by applying a function to the successful
    result of this future. If this future is completed with an exception
    then the new future will also contain this exception.

    we choose this path, that is, to use Action.async and be forced to return a future because
    we are declaring a sequence of events to happen later. and that sequence of events (map, then OK)
    is a future in and of itself. so what we avoid is ever having to RESOLVE a future
    keeping everything completely asyncrhounous and non blocking

    if we were to just use an Action, and return, say results.onComplete,
    swe would be waiting for the promise to resolve and blocking the thread

    If you'd like to learn more about netty, the async technology play is built on, have fun:
    https://livebook.manning.com/book/netty-in-action/chapter-1/18
     */
    results.map { animals =>
      Ok(Json.toJson(animals))
    }
  }
}
