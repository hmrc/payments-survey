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

package langswitch

import action.Actions
import play.api.i18n.I18nSupport
import play.api.mvc._
import play.mvc.Http.HeaderNames
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController
import views.DefaultViews

import javax.inject.Inject
import scala.concurrent.ExecutionContext

class LanguageSwitchController @Inject() (
    actions:      Actions,
    cc:           MessagesControllerComponents,
    defaultViews: DefaultViews
)(implicit ec: ExecutionContext) extends FrontendController(cc) with I18nSupport {

  def switchToLanguage(language: Language): Action[AnyContent] = cc.actionBuilder { implicit request =>
    val result: Result = request.headers.get(HeaderNames.REFERER) match {
      case Some(referrer) => Redirect(referrer)
      case None           => Redirect(routes.LanguageSwitchController.notFound)
    }
    result.withLang(language.toPlayLang)
  }

  def notFound: Action[AnyContent] = cc.actionBuilder { implicit request =>
    Ok(defaultViews.notFound)
  }
}

