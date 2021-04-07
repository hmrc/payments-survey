/*
 * Copyright 2021 HM Revenue & Customs
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

package action

import com.google.inject.Inject
import config.AppConfig
import journeylogger.JourneyLogger
import payapi.cardpaymentjourney.PayApiConnector
import play.api.mvc._
import requests.{JourneyRequest, RequestSupport}
import uk.gov.hmrc.http.HeaderCarrier
import views.DefaultViews

import scala.concurrent.{ExecutionContext, Future}

final class GetJourneyActionFactory @Inject() (
    paymentApi:   PayApiConnector,
    defaultViews: DefaultViews
)(implicit ec: ExecutionContext, config: AppConfig) {

  import RequestSupport._

  def journeyActionRefiner: ActionRefiner[Request, JourneyRequest] =
    new ActionRefiner[Request, JourneyRequest] {

      override protected def refine[A](request: Request[A]): Future[Either[Result, JourneyRequest[A]]] = {
        implicit val r: Request[A] = request

        for {
          isDefinedSessionId <- Future(implicitly[HeaderCarrier].sessionId.isDefined)
          maybeJourney <- if (isDefinedSessionId) paymentApi.findLatestJourneyBySessionId() else Future(None)
        } yield maybeJourney match {
          case Some(journey) => Right(new JourneyRequest(journey, request))
          case None =>
            if (isDefinedSessionId) {
              JourneyLogger.info(s"Session expired thus the Journey was not found")
            } else {
              JourneyLogger.error(s"Journey not found for that session id. Investigate what happened.")
            }
            //TODO revert this back to notfound when sessionIdFilter is removed
            //Left(Results.NotFound(defaultViews.notFound))
            Left(Results.Unauthorized(defaultViews.timedOutSessionId))
        }
      }

      override protected def executionContext: ExecutionContext = ec
    }

}
