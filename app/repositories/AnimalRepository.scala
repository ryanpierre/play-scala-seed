package repositories

import models.Animal
import javax.inject._
import uk.gov.hmrc.mongo.MongoComponent
import scala.concurrent.ExecutionContext
import org.mongodb.scala.model.Indexes.ascending
import uk.gov.hmrc.mongo.play.json.PlayMongoRepository
import org.bson.codecs.UuidCodec
import org.bson.UuidRepresentation
import scala.concurrent.Future
import play.api.Configuration
import java.util.concurrent.TimeUnit
import org.mongodb.scala.model.{ IndexModel, IndexOptions}
import scala.concurrent.duration.Duration

@Singleton
class AnimalRepository @Inject()(
  mongoComponent: MongoComponent,
  config: Configuration
)(implicit ec: ExecutionContext
) extends PlayMongoRepository[Animal](
  collectionName = "animal",
  mongoComponent = mongoComponent,
  indexes = AnimalRepository.indexes(config),
  domainFormat   = Animal.format,
  replaceIndexes = true,
  extraCodecs    = Seq(
                     new UuidCodec(UuidRepresentation.STANDARD)
                   )
) {
  def findAll(): Future[Seq[Animal]] =
    collection.find().toFuture

  def insert(animal: Animal): Future[Boolean] =
    collection
      .insertOne(animal)
      .toFuture
      .map(_ => true)
}

object AnimalRepository {

    def cacheTtl(config: Configuration): Long =
    Duration(config.get[Int]("mongodb.submission.timeToLiveInDays"), "days").toSeconds

  def indexes(config: Configuration) = Seq(
    IndexModel(
      ascending("lastUpdated"),
      IndexOptions()
        .name("animal-last-updated-index")
        .expireAfter(cacheTtl(config), TimeUnit.SECONDS)
    )
  )
}