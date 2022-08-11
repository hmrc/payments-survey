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
import paysurvey.journey.{SurveyJourneyId, JourneyService}
import play.api.mvc._
import requests.{RequestSupport, SurveyRequest}

import scala.concurrent.{ExecutionContext, Future}

final class GetJourneyActionFactory @Inject() (
    journeyService: JourneyService
)(implicit ec: ExecutionContext, config: AppConfig) {

  import RequestSupport._

  def maybeSurveyJourneyActionRefiner(id: SurveyJourneyId): ActionRefiner[Request, SurveyRequest] =
    new ActionRefiner[Request, SurveyRequest] {

      override protected def refine[A](request: Request[A]): Future[Either[Result, SurveyRequest[A]]] = {
        implicit val r: Request[A] = request
        for {
          maybeJourney <- journeyService.findLatestByJourneyId(id)
          maybeSurveyRequest <- maybeJourney match {
            case Some(j) => Future.successful {
              Option(SurveyRequest(j.content, j.audit, j.origin, j.returnMsg, j.returnHref, r))
            }
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
