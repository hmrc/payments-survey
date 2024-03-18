/*
 * Copyright 2024 HM Revenue & Customs
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

import action.Actions
import config.AppConfig
import play.api.Logger
import play.api.libs.json.Json
import play.api.mvc.{Action, MessagesControllerComponents}
import requests.RequestSupport
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
final class SsjController @Inject() (
    actions:    Actions,
    ssjService: SsjService,
    cc:         MessagesControllerComponents
)(implicit
    ec: ExecutionContext,
  appConfig: AppConfig
) extends FrontendController(cc) {

  private lazy val logger: Logger = Logger(getClass)

  def startJourney(): Action[SsjJourneyRequest] = Action.async(parse.json[SsjJourneyRequest]) {
    implicit request =>
      val ssjJourneyRequest: SsjJourneyRequest = request.body
      for {
        ssjResponse <- ssjService.startJourney(ssjJourneyRequest)
      } yield Created(Json.toJson(ssjResponse))
  }

}
