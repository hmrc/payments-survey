package paysurvey.journey.ssj

import akka.http.scaladsl.model.headers.Origin
import config.AppConfig
import payapi.cardpaymentjourney.model.journey.Url
import paysurvey.journey.{JourneyIdGenerator, JourneyService, SurveyJourney}
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

    val journey: SurveyJourney = ssjRequest.toJourney(
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
      journey.journeyId,
      Url(s"$frontendBaseUrl/survey")
    )
  }

  def startJourney(ssjRequest: SsjJourneyRequest)(implicit r: Request[_]): Future[SsjResponse] = {
    val sessionId = hc.sessionId.getOrElse {
      throw new Exception(s"The sessionId has to be provided [$ssjRequest.origin] [${ssjRequest.audit.toMap}]")
    }

    val journey = ssjRequest.toSurveyJourney(
      journeyId = journeyIdGenerator.nextJourneyId(),
      //todo hard code for now will remove when we remove pay-api stuff
      SurveyOrigin.Itsa,
      createdOn = LocalDateTime.now(clock),
      ssjRequest.contentOptions,
      sessionId
    )

    for {
      _ <- journeyService.insert(journey)
      //track who really started journey
      _ = logger.info(s"Started payment journey for origin: $ssjRequest.origin " +
        s"[JourneyOrigin: ${journey.origin}] " +
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
