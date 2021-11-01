package paysurvey.journey

import repository.RepoResultChecker.WriteResultChecker

import java.time.Clock
import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class JourneyService @Inject() (
    journeyRepo: JourneyRepo,
    clock:       Clock
)(implicit ec: ExecutionContext) {

  def insert(j: SurveyJourney): Future[Unit] = journeyRepo.insert(j).checkResult

  def findLatestBySessionId(sessionId: SessionId): Future[Option[SurveyJourney]] = {
    //    val createdOn = LocalDateTime.now(clock)

    journeyRepo.findLatestJourney(sessionId)
  }

}
