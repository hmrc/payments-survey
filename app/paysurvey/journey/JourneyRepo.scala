package paysurvey.journey

import play.api.libs.json.Json
import play.modules.reactivemongo.ReactiveMongoComponent
import reactivemongo.api.ReadPreference
import reactivemongo.api.indexes.{Index, IndexType}
import repository.Repo

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
final class JourneyRepo @Inject() (reactiveMongoComponent: ReactiveMongoComponent)(implicit ec: ExecutionContext)
  extends Repo[SurveyJourney, SurveyJourneyId]("journey", reactiveMongoComponent) {

  override def indexes: Seq[Index] = JourneyRepo.sessionIdIndexes
  //todo change it to use the journey id
  /**
   * Find the latest journey for given sessionId.
   */
  def findLatestJourneyBySessionId(sessionId: SessionId): Future[Option[SurveyJourney]] = {
    collection
      .find(Json.obj("sessionId" -> sessionId), None)
      .sort(Json.obj("createdOn" -> -1))
      .one(ReadPreference.primaryPreferred)(domainFormatImplicit, implicitly)
  }
  def findLatestJourneyByJourneyId(journeyId: SurveyJourneyId): Future[Option[SurveyJourney]] = {
    collection
      .find(Json.obj("journeyId" -> journeyId), None)
      .sort(Json.obj("createdOn" -> -1))
      .one(ReadPreference.primaryPreferred)(domainFormatImplicit, implicitly)
  }

}

object JourneyRepo {
  val sessionIdIndexes = Seq(
    Index (
      key  = Seq("sessionId" -> IndexType.Ascending),
      name = Some("sessionId")
    ),
    Index (
      key  = Seq("journeyId" -> IndexType.Ascending),
      name = Some("journeyId")
    )
  )
}
