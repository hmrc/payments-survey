/*
 * Copyright 2023 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
      _ = logger.info(s"Started payment journey for origin: ${ssjRequest.origin.toString}" +
        s"[JourneyOrigin: ${ssjRequest.origin}] " +
        s"[Reference: ${journey.audit.journey.toString}] " +
        s"[IsUserLoggedIn: ${hc.authorization.isDefined.toString}] " +
        s"[${hc.requestId.toString}] " +
        s"[${hc.sessionId.toString}] " +
        s"[${hc.requestChain.toString}] " +
        s"[${hc.forwarded.toString}] " +
        s"[trueClientIp: ${hc.trueClientIp.toString}] ")
    } yield {
      val id = journey.journeyId
      SsjResponse(
        id,
        Url(s"$frontendBaseUrl/v2/survey/${id.value}")
      )
    }
  }

}
