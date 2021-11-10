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

import cats.data.OptionT
import com.google.inject.Inject
import config.AppConfig
import model.content.{ContentOptions, JsdToContentOptions}
import payapi.cardpaymentjourney.PayApiConnector
import payapi.cardpaymentjourney.model.journey.{Journey, JourneySpecificData}
import paysurvey.audit.AuditOptions
import paysurvey.journey.ssj.SsjService
import paysurvey.journey.{JourneyService, SessionId, SurveyJourney}
import paysurvey.origin.SurveyOrigin
import play.api.mvc._
import requests.RequestSupport.sessionId
import requests.{MaybeJourneyRequest, RequestSupport, SurveyRequest}
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.{ExecutionContext, Future}

final class GetJourneyActionFactory @Inject() (
    journeyService: JourneyService,
    paymentApi:     PayApiConnector
)(implicit ec: ExecutionContext, config: AppConfig) {

  import RequestSupport._

  def maybeSurveyActionRefiner: ActionRefiner[Request, SurveyRequest] =
    new ActionRefiner[Request, SurveyRequest] {

      override protected def refine[A](request: Request[A]): Future[Either[Result, SurveyRequest[A]]] = {
        implicit val r: Request[A] = request
        val maybeSessionId = implicitly[HeaderCarrier].sessionId

          def fromPayApi(j: Journey[JourneySpecificData]) = {
            SurveyRequest(
              JsdToContentOptions.toContentOptions(j.journeySpecificData),
              AuditOptions.fromPayApi(j),
              Option(SurveyOrigin.PayApi(j.origin)),
              r
            )
          }

        for {
          maybeJourney <- maybeSessionId match {
            case Some(sessionId) => journeyService.findLatestBySessionId(sessionId)
            case None            => Future.successful(None)
          }

          maybeSurveyRequest <- maybeJourney match {
            case Some(j) => Future.successful {
              Option(SurveyRequest(j.content, j.audit, Option(j.origin), r))
            }
            case None if maybeSessionId.isDefined => paymentApi.findLatestJourneyBySessionId()
              .map(_.map(fromPayApi))
            case _ => Future.successful {
              None
            }
          }
        } yield maybeSurveyRequest match {
          case Some(request) => Right(request)
          case None          => Right(SurveyRequest.default)
        }
      }

      override protected def executionContext: ExecutionContext = ec
    }

}
