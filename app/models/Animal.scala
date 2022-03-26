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

object Animal {
  implicit val mongoDateTime: Format[LocalDateTime] =
    MongoJavatimeFormats.localDateTimeFormat
  implicit val format: OFormat[Animal] = Json.format[Animal]
}
