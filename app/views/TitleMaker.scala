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

package views

import payapi.cardpaymentjourney.model.journey.{Journey, JourneySpecificData}
import play.api.data.Form
import play.api.i18n.Messages
import model.JourneyExt

object TitleMaker {
  def titleMaker(h1Key: String, maybeJourney: Option[Journey[JourneySpecificData]], maybeForm: Option[Form[_]] = None)(implicit messages: Messages): String =
    makeTitle(h1Key, maybeJourney.flatMap(_.contentOptions.title.forCurrentLanguage), maybeForm.exists(_.hasErrors))

  private def makeTitle(h1Key: String, serviceName: Option[String], error: Boolean)(implicit messages: Messages): String = {
    val title = s"""${Messages(h1Key)} - ${serviceName.getOrElse(Messages("global.service-name"))} - ${Messages("global.title-suffix")}"""
    if (error) s"""${Messages("global.error-prefix")} - $title""" else title
  }
}
