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

package paysurvey.journey.ssj

import action.Actions
import config.AppConfig
import paysurvey.origin.SurveyOrigin
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

  def start(origin: SurveyOrigin): Action[SsjRequest] = Action.async(parse.json[SsjRequest]) {
    implicit request =>
      val ssjRequest = request.body
      for {
        ssjResponse <- ssjService.start(origin, ssjRequest)
      } yield Created(Json.toJson(ssjResponse))
  }

  def startJourney(): Action[SsjJourneyRequest] = Action.async(parse.json[SsjJourneyRequest]) {
    implicit request =>
      val ssjJourneyRequest: SsjJourneyRequest = request.body
      //todo what do I need to make this entry in survey
      for {
        ssjResponse <- ssjService.startJourney(ssjJourneyRequest)
      } yield Created(Json.toJson(ssjResponse))
  }

}
