/*
 * Copyright 2020 HM Revenue & Customs
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
import model._
import payapi.cardpaymentjourney.PayApiConnector
import payapi.corcommon.model.{JourneyId, Origins}
import play.api.Logger
import play.api.mvc._
import requests.{JourneyRequest, RequestSupport}
import uk.gov.hmrc.auth.core.{AuthorisationException, AuthorisedFunctions}
import uk.gov.hmrc.http.{HeaderCarrier, HttpException, SessionKeys}
import uk.gov.hmrc.play.HeaderCarrierConverter
import views.DefaultViews

import scala.concurrent.{ExecutionContext, Future}

final class GetJourneyActionFactory @Inject() (
    af:           AuthorisedFunctions,
    paymentApi:   PayApiConnector,
    defaultViews: DefaultViews
)(implicit ec: ExecutionContext, config: AppConfig) {

  import RequestSupport._

  def journeyActionRefiner: ActionRefiner[Request, JourneyRequest] =
    new ActionRefiner[Request, JourneyRequest] {

      override protected def refine[A](request: Request[A]): Future[Either[Result, JourneyRequest[A]]] = {
        implicit val r: Request[A] = request

        for {
          maybeJourney <- paymentApi.findLatestJourneyBySessionId()
        } yield maybeJourney match {
          case Some(journey) => Right(new JourneyRequest(journey, request))
          case None =>
            JourneyLogger.error(s"Journey not found for that session id. Investigate what happened.")
            Left(Results.NotFound(defaultViews.notFound))
        }
      }

      override protected def executionContext: ExecutionContext = ec
    }

}
