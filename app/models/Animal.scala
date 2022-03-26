package models

import play.api.libs.json._
import uk.gov.hmrc.mongo.play.json.formats.MongoJavatimeFormats
import java.time.LocalDateTime

case class Animal(
    _id: UniqueId,
    nickname: String,
    animalType: String,
    created: LocalDateTime,
    lastUpdated: LocalDateTime
)

// Why do we have to do all this madness ? recall that JSON stands for:
    // JavaScript Object Notation
// Which means only javascript can actually *natively* read this
// To make matters worse, we're in a strongly typed language that needs to know 
// The type of everything, and so it needs a blueprint to do that. Luckily, all primitives
// are basically built in so Json.format[ModelName] should be enough. 

// And, when you have custom types like UniqueId 
// (which I added to ensure that _id is always a unique uuid not just a string)
// You need to write a custom resolver (read and write methods) to convert to and from json
// Which will automatically be called by format :)

// We also need this mongoDateTime thing which could actually be called anything all it needs
// to be is of the type Format[TypeYouWantToCreateAFormatterFor] and it will be automatically
// used when we attempt to format that type. Here were using a custom hmrc formatter
object Animal {
  implicit val mongoDateTime: Format[LocalDateTime] =
    MongoJavatimeFormats.localDateTimeFormat
  implicit val format: OFormat[Animal] = Json.format[Animal]
}
