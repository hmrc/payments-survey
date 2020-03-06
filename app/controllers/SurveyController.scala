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

import javax.inject.{Inject, Singleton}
import model.SurveyForm
import model.SurveyForm.surveyForm
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import uk.gov.hmrc.play.audit.http.connector.AuditResult
import uk.gov.hmrc.play.audit.model._
import uk.gov.hmrc.play.bootstrap.controller.FrontendController

import scala.concurrent.Future

@Singleton
final class SurveyController @Inject() (cc: MessagesControllerComponents) extends FrontendController(cc) {

  def route(paymentStatus: String): Action[AnyContent] = Action { implicit request =>
    paymentStatus match {
      case "Successful" => Redirect(routes.SurveyController.survey())
      case "Failed" => Redirect(routes.SurveyController.surveyForRejected())
      case "Cancelled" => Redirect(routes.SurveyController.surveyForCancelled())
    }
  }

  def survey: Action[AnyContent] = Action.async { implicit request =>
    Future.successful(Ok(views.html.survey(surveyForm, journey = "completed")))
  }

  def surveyForRejected: Action[AnyContent] = Action.async { implicit request =>
    Future.successful(Ok(views.html.survey(surveyForm, journey = "rejected")))
  }

  def surveyForCancelled: Action[AnyContent] = Action.async { implicit request =>
    Future.successful(Ok(views.html.survey(surveyForm, journey = "cancelled")))
  }

  def submitSurvey(journey: String): Action[AnyContent] = Action.async { implicit request =>
    Future.successful {
      implicit val hc = request.headerCarrier

      surveyForm.bindFromRequest().fold(
        formWithErrors => { BadRequest(views.html.survey(formWithErrors, journey)) },
        data => {
          val SurveyForm(wereYouAble, overallRate, howEasyAnswer, journey, comments) = data
          val surveyMap: Map[String, String] = Map(
            "wereYouAble" -> wereYouAble.toString,
            "overallRate" -> overallRate.toString,
            "howEasy" -> howEasyAnswer.toString,
            "comments" -> comments,
            "journey" -> journey)

          val orderId = for {
            pd <- request.paymentDetails
            wpRef <- pd.wpRef
          } yield wpRef

          val userType = if (request.isLoggedIn) "LoggedIn" else "LoggedOut"

          val details = hc.toAuditDetails("orderId" -> orderId.getOrElse("?"), "userType" -> userType) ++
            surveyMap ++
            hc.toAuditDetails("liability" -> request.tax.shortName)

          val event: Future[AuditResult] = auditConnector.sendEvent(
            DataEvent(
              auditSource = "payments-frontend",
              auditType = "Questionnaire",
              tags = hc.toAuditTags("submitSurvey", request.uri),
              detail = details))

          Redirect(controllers.routes.SurveyController.surveyThanks())
        })
    }
  }

  def surveyThanks: Action[AnyContent] = Action.async { implicit request =>
    Future.successful(Ok(views.html.survey_thanks()))
  }
}
