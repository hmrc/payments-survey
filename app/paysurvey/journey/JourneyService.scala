package paysurvey.journey

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class JourneyService @Inject() (
    journeyRepo: JourneyRepo
)(implicit ec: ExecutionContext) {

  def insert(j: SurveyJourney): Future[Unit] = journeyRepo.insert(j) //.checkResult

  def findLatestByJourneyId(journeyId: SurveyJourneyId): Future[Option[SurveyJourney]] = {
    journeyRepo.findLatestJourneyByJourneyId(journeyId)
  }

}
