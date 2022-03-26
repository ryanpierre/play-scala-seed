package models

import play.api.libs.json.{__, JsString, Reads, Writes}
import play.api.mvc.PathBindable

import java.util.UUID

case class UniqueId(value: String)

object UniqueId {
  def apply(): UniqueId                 = UniqueId(UUID.randomUUID().toString)
  implicit val writes: Writes[UniqueId] = UniqueId => JsString(UniqueId.value)
  implicit val reads: Reads[UniqueId]   = __.read[String].map(id => UniqueId(id))

  implicit lazy val pathBindable: PathBindable[UniqueId] = new PathBindable[UniqueId] {
    override def bind(key: String, value: String): Either[String, UniqueId] =
      implicitly[PathBindable[String]].bind(key, value).right.map(UniqueId(_))

    override def unbind(key: String, value: UniqueId): String =
      value.value
  }
}
