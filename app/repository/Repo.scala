package repository

import paysurvey.journey.{SurveyJourney, SurveyJourneyId}
import play.api.libs.json.{Format, OFormat}
import uk.gov.hmrc.mongo.MongoComponent
import uk.gov.hmrc.mongo.play.json.PlayMongoRepository
import scala.concurrent.ExecutionContext
import org.mongodb.scala.model.IndexModel

abstract class Repo(
    collectionName: String,
    mongo:          MongoComponent,
    indexes:        Seq[IndexModel]
)
  (implicit
    domainFormat: OFormat[SurveyJourney],
   idFormat: Format[SurveyJourneyId],
   ec:       ExecutionContext
)
  extends PlayMongoRepository[SurveyJourney](
    mongoComponent = mongo,
    collectionName = collectionName,
    domainFormat   = domainFormat,
    indexes        = indexes,
    replaceIndexes = false
  )
