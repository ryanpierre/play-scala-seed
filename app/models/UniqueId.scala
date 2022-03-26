package models

import play.api.libs.json.{__, JsString, Reads, Writes}
import play.api.mvc.PathBindable

import java.util.UUID

case class UniqueId(value: String)

object UniqueId {
  def apply(): UniqueId                 = UniqueId(UUID.randomUUID().toString)
  implicit val writes: Writes[UniqueId] = UniqueId => JsString(UniqueId.value)
  implicit val reads: Reads[UniqueId]   = __.read[String].map(id => UniqueId(id))
}
