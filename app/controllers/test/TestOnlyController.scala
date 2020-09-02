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

package controllers.test

import javax.inject.{Inject, Singleton}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import uk.gov.hmrc.play.bootstrap.controller.FrontendController

import scala.concurrent.ExecutionContext

@Singleton
class TestOnlyController @Inject() (
    cc:               MessagesControllerComponents,
    show_error_pages: views.html.error.show_error_pages,
    error_5xx:        views.html.error.error_5xx
)(
    implicit
    ec: ExecutionContext
) extends FrontendController(cc) {

  def addToSession(key: String, value: String): Action[AnyContent] = Action { implicit request =>
    Ok("").addingToSession(key -> value)
  }

  def showErrorPages(): Action[AnyContent] = Action { implicit request =>
    Ok(show_error_pages())
  }

  def showError5xx(): Action[AnyContent] = Action { implicit request =>
    Ok(error_5xx())
  }
}
