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

package requests

import javax.inject.Inject
import model.langswitch.Language
import play.api.i18n._
import play.api.mvc.{Request, RequestHeader}
import uk.gov.hmrc.http.{HeaderCarrier, SessionKeys}
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendHeaderCarrierProvider

/**
 * Repeating the pattern which was brought originally by play-framework
 * and putting some more data which can be derived from a request
 *
 * Use it to provide HeaderCarrier, Lang, or Messages
 */
class RequestSupport @Inject() (override val messagesApi: MessagesApi) extends I18nSupport {

  implicit def hc(implicit request: Request[_]): HeaderCarrier = RequestSupport.hc
  def lang(implicit messages: Messages): Lang = messages.lang
  def language(implicit messages: Messages): Language = Language()(messages)
}

object RequestSupport {

  implicit def hc(implicit request: Request[_]): HeaderCarrier = HcProvider.headerCarrier

  /**
   * Naive way of checking if user is logged in. Use it in views only.
   */
  def isLoggedIn(implicit rh: RequestHeader): Boolean = rh.session.get(SessionKeys.authToken).isDefined

  /**
   * Naive way of getting session id. Use it in views only.
   */
  def sessionId(implicit request: Request[_]): String = request.session.get(SessionKeys.sessionId).getOrElse("Unknown Session Id")

  /**
   * This is because we want to give responsibility of creation of [[HeaderCarrier]] to the platform code.
   * If they refactor how hc is created our code will pick it up automatically.
   */
  private object HcProvider extends FrontendHeaderCarrierProvider {
    def headerCarrier(implicit request: Request[_]): HeaderCarrier = hc(request)
  }
}
