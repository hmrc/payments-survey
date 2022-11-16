package repository

import play.api.libs.json.{Format, JsObject, Json, OFormat, OWrites}
import uk.gov.hmrc.mongo.MongoComponent
import uk.gov.hmrc.mongo.play.json.PlayMongoRepository
//import play.modules.reactivemongo.ReactiveMongoComponent
//import reactivemongo.api.commands.WriteResult
//import uk.gov.hmrc.mongo.ReactiveRepository
import uk.gov.hmrc.play.http.logging.Mdc

import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.ExecutionContext.Implicits.global // Somewhat doubtful about this
import org.mongodb.scala.model.IndexModel

abstract class Repo[A, ID](
                            collectionName: String,
                            mongo: MongoComponent,
                            indexes: Seq[IndexModel]
)(implicit
  manifest: Manifest[A],
  mid:          Manifest[ID],
  domainFormat: OFormat[A],
  idFormat:     Format[ID]
)
  extends PlayMongoRepository[A](
    mongoComponent = mongo,
    collectionName = collectionName,
    //mongo.mongoConnector.db,
    domainFormat = domainFormat,
    indexes = indexes,
    replaceIndexes = false
    //idFormat
  ) {

  private def idSelector(id: ID): JsObject = Json.obj("_id" -> id)

  implicit val f: OWrites[JsObject] = new OWrites[JsObject] {
    override def writes(o: JsObject): JsObject = o
  }

  def upsert(id: ID, a: A)(implicit ec: ExecutionContext): Future[Unit] = Mdc.preservingMdc {
    collection.update(ordered = false).one(
      idSelector(id),
      a,
      upsert = true
    ).checkResult
  }

  private implicit class WriteResultChecker(future: Future[WriteResult]) {
    def checkResult(implicit ec: ExecutionContext): Future[Unit] = future.map { writeResult =>
      if (hasAnyConcerns(writeResult)) throw new RuntimeException(writeResult.toString)
      else ()
    }
  }

  private def hasAnyConcerns(writeResult: WriteResult): Boolean = !writeResult.ok || writeResult.writeErrors.nonEmpty || writeResult.writeConcernError.isDefined

}
