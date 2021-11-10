package paysurvey.journey.ssj

import config.AppConfig
import payapi.cardpaymentjourney.PayApiConnector
import payapi.cardpaymentjourney.model.journey.Url
import paysurvey.journey.{JourneyIdGenerator, JourneyService}
import paysurvey.origin.SurveyOrigin
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

  def start(origin: SurveyOrigin, ssjRequest: SsjRequest)(implicit r: Request[_]): Future[SsjResponse] = {
    val sessionId = hc.sessionId.getOrElse {
      throw new Exception(s"The sessionId has to be provided [$origin] [${ssjRequest.audit.toMap}]")
    }

    val journey = ssjRequest.toJourney(
      journeyId = journeyIdGenerator.nextJourneyId(),
      origin,
      createdOn = LocalDateTime.now(clock),
      sessionId
    )

    for {
      _ <- journeyService.insert(journey)
      //track who really started journey
      _ = logger.info(s"Started payment journey for origin: $origin " +
        s"[JourneyOrigin: ${journey.origin}] " +
        s"[Reference: ${journey.audit.journey}] " +
        s"[IsUserLoggedIn: ${hc.authorization.isDefined}] " +
        s"[${hc.requestId}] " +
        s"[${hc.sessionId}] " +
        s"[${hc.requestChain}] " +
        s"[${hc.forwarded}] " +
        s"[trueClientIp: ${hc.trueClientIp}] ")
    } yield SsjResponse(
      journey._id,
      Url(s"$frontendBaseUrl/survey")
    )
  }

}
