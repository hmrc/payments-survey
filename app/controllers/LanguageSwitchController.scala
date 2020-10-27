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
import model._
import langswitch.Language
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import requests.RequestSupport
import uk.gov.hmrc.play.audit.http.connector.AuditConnector
import javax.inject.Inject
import play.mvc.Http.HeaderNames
import uk.gov.hmrc.play.bootstrap.controller.FrontendController
import views.DefaultViews

import scala.concurrent.ExecutionContext

class LanguageSwitchController @Inject() (
    actions:        Actions,
    auditConnector: AuditConnector,
    cc:             MessagesControllerComponents,
    requestSupport: RequestSupport,
    error404:       views.html.error.error_404,
    defaultViews:   DefaultViews
)(implicit
    ec: ExecutionContext,
  appConfig: AppConfig
) extends FrontendController(cc) {

  import requestSupport._

  def switchToLanguage(language: Language): Action[AnyContent] = actions.journeyAction { implicit request =>

    val maybeReferrer: Option[String] =
      request
        .headers
        .get(HeaderNames.REFERER)

    maybeReferrer.fold(Redirect(controllers.routes.LanguageSwitchController.notFound).withLang(language.toPlayLang))(Redirect(_).withCookies().withLang(language.toPlayLang))
  }

  def notFound: Action[AnyContent] = actions.journeyAction { implicit request =>
    Ok(defaultViews.notFound)
  }

}
