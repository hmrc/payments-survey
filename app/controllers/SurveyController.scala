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

package controllers

import action.Actions
import config.AppConfig
import model.SurveyForm.surveyForm
import model._
import paysurvey.journey.{JourneyService, SurveyJourneyId}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import requests.RequestSupport
import uk.gov.hmrc.play.audit.http.connector.AuditConnector
import uk.gov.hmrc.play.audit.model._
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
final class SurveyController @Inject() (
    actions:        Actions,
    auditConnector: AuditConnector,
    cc:             MessagesControllerComponents,
    requestSupport: RequestSupport,
    survey:         views.html.survey,
    surveyJourney:  views.html.survey_journey,
    surveyThanks:   views.html.survey_thanks
)(implicit
    ec: ExecutionContext,
  appConfig: AppConfig
) extends FrontendController(cc) {

  import requestSupport._

  def survey: Action[AnyContent] = actions.maybeSurveyAction { implicit request =>
    Ok(survey(surveyForm))
  }

  def surveyJourney(id: SurveyJourneyId): Action[AnyContent] = actions.maybeSurveyJourneyAction(id) { implicit request =>

    Ok(surveyJourney(surveyForm, "backLinkHref", "backLinkMessage"))
  }

  def submitSurvey: Action[AnyContent] = actions.maybeSurveyAction { implicit request =>
    surveyForm.bindFromRequest().fold(
      formWithErrors => { BadRequest(survey(formWithErrors)) },
      data => {
        val surveyMap: Map[String, String] = Map(
          "wereYouAble" -> data.wereYouAble,
          "overallRate" -> data.overallRate,
          "howEasy" -> data.howEasy,
          "comments" -> data.comments.getOrElse("None given")
        )

        val details = surveyMap ++ request.audit.toMap

        auditConnector.sendEvent(
          DataEvent(
            auditSource = "payments-survey",
            auditType   = "Questionnaire",
            tags        = Map("submitSurvey" -> request.uri),
            detail      = details
          )
        )

        Redirect(controllers.routes.SurveyController.showSurveyThanks)
      }
    )
  }

  def showSurveyThanks: Action[AnyContent] = actions.maybeSurveyAction { implicit request =>
    Ok(surveyThanks())
  }
}
