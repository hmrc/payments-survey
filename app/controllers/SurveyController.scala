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
import javax.inject.{Inject, Singleton}
import journeylogger.JourneyLogger
import model.SurveyForm.surveyForm
import model._
import model.langswitch.Language
import payapi.corcommon.model.JourneyId
import play.api.i18n.Lang
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import requests.RequestSupport
import uk.gov.hmrc.play.audit.http.connector.AuditConnector
import uk.gov.hmrc.play.audit.model._
import uk.gov.hmrc.play.bootstrap.controller.FrontendController

import scala.concurrent.ExecutionContext

@Singleton
final class SurveyController @Inject() (
    actions:        Actions,
    auditConnector: AuditConnector,
    cc:             MessagesControllerComponents,
    requestSupport: RequestSupport,
    survey:         views.html.survey,
    surveyThanks:   views.html.survey_thanks
)(implicit
    ec: ExecutionContext,
  appConfig: AppConfig
) extends FrontendController(cc) {

  import requestSupport._

  def survey: Action[AnyContent] = actions.journeyAction { implicit request =>
    Ok(survey(surveyForm)).withLang(Lang("en"))
  }

  def switchToLanguage(language: Language): Action[AnyContent] = actions.journeyAction { implicit request =>
    //Ok(survey(surveyForm)).withLang(language.toPlayLang)
    Ok(survey(surveyForm)).withLang(Lang("en"))
  }

  def submitSurvey: Action[AnyContent] = actions.journeyAction { implicit request =>
    surveyForm.bindFromRequest().fold(
      formWithErrors => { BadRequest(survey(formWithErrors)) },
      data => {
        val surveyMap: Map[String, String] = Map(
          "wereYouAble" -> data.wereYouAble.toString,
          "overallRate" -> data.overallRate.toString,
          "howEasy" -> data.howEasy.toString,
          "comments" -> data.comments,
          "journey" -> data.journey
        )

        val userType: String = if (RequestSupport.isLoggedIn) "LoggedIn" else "LoggedOut"

        if (request.journey.reference.isEmpty) JourneyLogger.error("Expected reference to be defined in the journey. Investigate what happened.")
        val referenceString = request.journey.reference.map(_.value).getOrElse("?")
        val details: Map[String, String] = Map("orderId" -> referenceString, "userType" -> userType) ++
          surveyMap ++
          Map("liability" -> request.journey.origin.auditName)

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

  def showSurveyThanks: Action[AnyContent] = actions.journeyAction { implicit request =>
    Ok(surveyThanks())
  }
}
