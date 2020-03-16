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

package controllers

import action.Actions
import config.AppConfig
import javax.inject.{ Inject, Singleton }
import model.SurveyForm.surveyForm
import model.{ SurveyForm, _ }
import payapi.corcommon.model.JourneyId
import play.api.mvc.{ Action, AnyContent, MessagesControllerComponents }
import requests.RequestSupport
import uk.gov.hmrc.play.audit.http.connector.AuditConnector
import uk.gov.hmrc.play.audit.model._
import uk.gov.hmrc.play.bootstrap.controller.FrontendController

import scala.concurrent.ExecutionContext

@Singleton
final class SurveyController @Inject() (
  actions: Actions,
  auditConnector: AuditConnector,
  cc: MessagesControllerComponents,
  requestSupport: RequestSupport,
  survey: views.html.survey,
  surveyThanks: views.html.survey_thanks)(implicit
  ec: ExecutionContext,
  appConfig: AppConfig) extends FrontendController(cc) {

  import requestSupport._

  def route(): Action[AnyContent] = Action { implicit request =>
    request.session
      .get("journeyId")
      .fold(Redirect(appConfig.payFrontendBaseUrl))(id => Redirect(routes.SurveyController.survey(JourneyId(id))))

  }

  def survey(journeyId: JourneyId): Action[AnyContent] = actions.journeyAction(journeyId) { implicit request =>
    Ok(survey(surveyForm))
  }

  def submitSurvey(journeyId: JourneyId): Action[AnyContent] = actions.journeyAction(journeyId) { implicit request =>
    surveyForm.bindFromRequest().fold(
      formWithErrors => { BadRequest(survey(formWithErrors)) },
      data => {
        val SurveyForm(wereYouAble, overallRate, howEasyAnswer, journey, comments) = data

        val surveyMap: Map[String, String] = Map(
          "wereYouAble" -> wereYouAble.toString,
          "overallRate" -> overallRate.toString,
          "howEasy" -> howEasyAnswer.toString,
          "comments" -> comments,
          "journey" -> journey)

        val userType: String = if (RequestSupport.isLoggedIn) "LoggedIn" else "LoggedOut"

        val details: Map[String, String] = Map("orderId" -> request.journey.reference.map(_.value).getOrElse("?"), "userType" -> userType) ++
          surveyMap ++
          Map("liability" -> request.journey.origin.auditName)

        auditConnector.sendEvent(
          DataEvent(
            auditSource = "payments-survey",
            auditType = "Questionnaire",
            tags = Map("submitSurvey" -> request.uri),
            detail = details))

        Redirect(controllers.routes.SurveyController.surveyThanks(journeyId))
      })
  }

  def surveyThanks(journeyId: JourneyId): Action[AnyContent] = actions.journeyAction(journeyId) { implicit request =>
    Ok(surveyThanks())
  }
}
