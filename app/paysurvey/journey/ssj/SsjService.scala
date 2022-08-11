package paysurvey.journey.ssj

import config.AppConfig
import payapi.cardpaymentjourney.model.journey.Url
import paysurvey.journey.{JourneyIdGenerator, JourneyService}
import play.api.Logger
import play.api.mvc.Request
import requests.RequestSupport.hc

import java.time.{Clock, LocalDateTime}
import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class SsjService @Inject() (
    journeyService:     JourneyService,
    journeyIdGenerator: JourneyIdGenerator,
    clock:              Clock,
    appConfig:          AppConfig
)(implicit ec: ExecutionContext) {

  import appConfig.frontendBaseUrl

  private lazy val logger: Logger = Logger(getClass)

  def startJourney(ssjRequest: SsjJourneyRequest)(implicit r: Request[_]): Future[SsjResponse] = {

    val journey = ssjRequest.toSurveyJourney(
      journeyId = journeyIdGenerator.nextJourneyId(),
      createdOn = LocalDateTime.now(clock),
      ssjRequest.contentOptions
    )

    for {
      _ <- journeyService.insert(journey)
      //track who really started journey
      _ = logger.info(s"Started payment journey for origin: $ssjRequest.origin " +
        s"[JourneyOrigin: ${ssjRequest.origin}] " +
        s"[Reference: ${journey.audit.journey}] " +
        s"[IsUserLoggedIn: ${hc.authorization.isDefined}] " +
        s"[${hc.requestId}] " +
        s"[${hc.sessionId}] " +
        s"[${hc.requestChain}] " +
        s"[${hc.forwarded}] " +
        s"[trueClientIp: ${hc.trueClientIp}] ")
    } yield {
      val id = journey.journeyId
      SsjResponse(
        id,
        Url(s"$frontendBaseUrl/v2/survey/${id.value}")
      )
    }
  }

}
