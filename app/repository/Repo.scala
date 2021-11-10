package repository

import play.api.libs.json.{Format, JsObject, Json, OFormat, OWrites}
import play.modules.reactivemongo.ReactiveMongoComponent
import reactivemongo.api.commands.WriteResult
import uk.gov.hmrc.mongo.ReactiveRepository
import uk.gov.hmrc.play.http.logging.Mdc

import scala.concurrent.{ExecutionContext, Future}

abstract class Repo[A, ID](
    collectionName:         String,
    reactiveMongoComponent: ReactiveMongoComponent
)(implicit
    manifest: Manifest[A],
  mid:          Manifest[ID],
  domainFormat: OFormat[A],
  idFormat:     Format[ID]
)
  extends ReactiveRepository[A, ID](
    collectionName,
    reactiveMongoComponent.mongoConnector.db,
    domainFormat,
    idFormat
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
