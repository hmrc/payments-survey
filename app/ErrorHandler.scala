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

import javax.inject.{Inject, Singleton}
import play.api.i18n.MessagesApi
import play.api.mvc._
import play.twirl.api.{Html, HtmlFormat}
import uk.gov.hmrc.play.bootstrap.frontend.http.FrontendErrorHandler

import scala.language.implicitConversions

@Singleton
class ErrorHandler @Inject() (
    override val messagesApi: MessagesApi,
    errorTemplate:            views.html.error.error_template,
    error404:                 views.html.error.error_404,
    error5xx:                 views.html.error.error_5xx,
    fallbackClientError:      views.html.error.error_fallback
) extends FrontendErrorHandler {

  override def notFoundTemplate(implicit request: Request[_]): HtmlFormat.Appendable = error404()

  override def internalServerErrorTemplate(implicit request: Request[_]): Html = error5xx()

  override def fallbackClientErrorTemplate(implicit request: Request[_]): Html = fallbackClientError()

  override def standardErrorTemplate(
      pageTitle: String,
      heading:   String,
      message:   String
  )(implicit request: Request[_]): Html = errorTemplate(
    pageTitle,
    heading,
    message
  )
}
