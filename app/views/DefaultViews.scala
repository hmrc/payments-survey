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

package views

import config.AppConfig
import javax.inject.Inject
import play.api.i18n.Messages
import play.api.mvc.Request
import play.twirl.api.HtmlFormat
import requests.RequestSupport

class DefaultViews @Inject() (error_template: views.html.error.error_template, requestSupport: RequestSupport)(implicit appConfig: AppConfig) {

  import requestSupport._

  def internalServerError()(implicit request: Request[_]): HtmlFormat.Appendable =
    error_template(
      Messages("error.InternalServerError.heading"),
      Messages("error.InternalServerError.heading"),
      Messages("error.InternalServerError.message"))

  def notFound(implicit request: Request[_]): HtmlFormat.Appendable =
    error_template(
      Messages("error.pageNotFound.title"),
      Messages("error.pageNotFound.heading"),
      Messages("error.pageNotFound.message"))

  def unauthorised()(implicit request: Request[_]): HtmlFormat.Appendable =
    error_template(
      Messages("error.pageNotFound.title"),
      Messages("error.pageNotFound.heading"),
      Messages("error.pageNotFound.message"))
}
