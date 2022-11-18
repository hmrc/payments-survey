package repository

import org.mongodb.scala.model.UpdateOptions
import paysurvey.journey.{SurveyJourney, SurveyJourneyId}
import play.api.libs.json.{Format, JsObject, Json, OFormat, OWrites}
import uk.gov.hmrc.mongo.MongoComponent
import uk.gov.hmrc.mongo.play.json.PlayMongoRepository
//import play.modules.reactivemongo.ReactiveMongoComponent
//import reactivemongo.api.commands.WriteResult
//import uk.gov.hmrc.mongo.ReactiveRepository
import uk.gov.hmrc.play.http.logging.Mdc

import scala.concurrent.{ExecutionContext, Future}
import org.mongodb.scala.model.IndexModel

abstract class Repo(
    collectionName: String,
    mongo:          MongoComponent,
    indexes:        Seq[IndexModel]
)(implicit
    //manifest:     Manifest[A],
    //mid:          Manifest[ID],
    domainFormat: OFormat[SurveyJourney],
  idFormat: Format[SurveyJourneyId],
  ec:       ExecutionContext
)
  extends PlayMongoRepository[SurveyJourney](
    mongoComponent = mongo,
    collectionName = collectionName,
    //mongo.mongoConnector.db,
    domainFormat   = domainFormat,
    indexes        = indexes,
    replaceIndexes = false
  //idFormat
  ) {

  private def idSelector(id: SurveyJourneyId): JsObject = Json.obj("_id" -> id)

  implicit val f: OWrites[JsObject] = new OWrites[JsObject] {
    override def writes(o: JsObject): JsObject = o
  }
}
