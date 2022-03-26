package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import repositories.AnimalRepository
import models.Animal
import models.UniqueId
import java.time.LocalDateTime
import play.api.libs.json.Json

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class AnimalController @Inject()(val controllerComponents: ControllerComponents, animalRepository: AnimalRepository) extends BaseController {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */

  def create() = Action { implicit request: Request[AnyContent] => 
    val newAnimal = new Animal(UniqueId(), "Ryan", "Giraffe", LocalDateTime.now(),LocalDateTime.now() )
    val success = animalRepository.insert(newAnimal) 
    Ok(Json.toJson(newAnimal))
  }

  def index() = Action { implicit request: Request[AnyContent] =>
    Ok("AnimalController OK!")
  }
}
