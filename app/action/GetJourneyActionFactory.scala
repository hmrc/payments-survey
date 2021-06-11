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
import payapi.cardpaymentjourney.PayApiConnector
import play.api.mvc._
import requests.{MaybeJourneyRequest, RequestSupport}
import uk.gov.hmrc.http.HeaderCarrier
import views.DefaultViews

import scala.concurrent.{ExecutionContext, Future}

final class GetJourneyActionFactory @Inject() (
    paymentApi: PayApiConnector
)(implicit ec: ExecutionContext, config: AppConfig) {

  import RequestSupport._

  def maybeJourneyActionRefiner: ActionRefiner[Request, MaybeJourneyRequest] =
    new ActionRefiner[Request, MaybeJourneyRequest] {

      override protected def refine[A](request: Request[A]): Future[Either[Result, MaybeJourneyRequest[A]]] = {
        implicit val r: Request[A] = request
        for {
          isDefinedSessionId <- Future(implicitly[HeaderCarrier].sessionId.isDefined)
          maybeJourney <- if (isDefinedSessionId) paymentApi.findLatestJourneyBySessionId() else Future(None)
        } yield maybeJourney match {
          case Some(journey) => Right(new MaybeJourneyRequest(Some(journey), request))
          case None          => Right(new MaybeJourneyRequest(None, request))
        }
      }

      override protected def executionContext: ExecutionContext = ec
    }

}
