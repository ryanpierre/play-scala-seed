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

/** This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
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
     */
    results.map { animals =>
      Ok(Json.toJson(animals))
    }
  }
}

