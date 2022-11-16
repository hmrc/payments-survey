package paysurvey.journey

import play.api.libs.json.Json
import play.modules.reactivemongo.ReactiveMongoComponent
import reactivemongo.api.ReadPreference
import reactivemongo.api.indexes.{Index, IndexType}
import repository.Repo

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}
import org.mongodb.scala.model.{IndexModel, IndexOptions}
import org.mongodb.scala.model.Indexes.ascending

@Singleton
final class JourneyRepo @Inject() (reactiveMongoComponent: ReactiveMongoComponent)(implicit ec: ExecutionContext)
  extends Repo[SurveyJourney, SurveyJourneyId]("journey", reactiveMongoComponent, Seq(IndexModel(ascending("value"),IndexOptions().unique(true))) {

  //override def indexes: Seq[Index] = JourneyRepo.journeyIdIndexes

  /**
   * Find the latest journey for given sessionId.
   */

  def findLatestJourneyByJourneyId(journeyId: SurveyJourneyId): Future[Option[SurveyJourney]] = {
    collection
      .find(Json.obj("journeyId" -> journeyId), None)
      .sort(Json.obj("createdOn" -> -1))
      .one(ReadPreference.primaryPreferred)(domainFormatImplicit, implicitly)
  }

}

object JourneyRepo {
  val journeyIdIndexes = Seq(
    Index (
      key  = Seq("journeyId" -> IndexType.Ascending),
      name = Some("journeyId")
    )
  )
}
