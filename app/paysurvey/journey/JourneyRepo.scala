package paysurvey.journey

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}
import org.mongodb.scala.model.{Filters, IndexModel, IndexOptions}
import org.mongodb.scala.model.Indexes.{ascending, descending}
import uk.gov.hmrc.mongo.MongoComponent
import uk.gov.hmrc.mongo.play.json.PlayMongoRepository

@Singleton
final class JourneyRepo @Inject() (mongo: MongoComponent)(implicit ec: ExecutionContext)
  extends PlayMongoRepository[SurveyJourney](
    mongoComponent = mongo,
    collectionName = "journey",
    indexes        = Seq(IndexModel(ascending("journeyId"))),
    domainFormat   = SurveyJourney.format,
    replaceIndexes = true //Todo: discuss
  ) {

  /**
   * Find the latest journey for given sessionId.
   */
  def findLatestJourneyByJourneyId(journeyId: SurveyJourneyId): Future[Option[SurveyJourney]] = {
    collection
      .withReadPreference(com.mongodb.ReadPreference.primary())
      .find(Filters.equal("journeyId", journeyId.value))
      .sort(descending("createdOn"))
      .toFuture().map(_.headOption)
  }

  def insert(surveyJourney: SurveyJourney): Future[Unit] = //Throw a new RuntimeException(writeResult.toString) if things go wrong
    collection
      .insertOne(surveyJourney)
      .toFuture()
      .map(result => if (result.wasAcknowledged()) () else throw new RuntimeException(result.toString))
}
