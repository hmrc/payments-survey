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

package model

import play.api.data.Form
import play.api.data.Forms.{ mapping, number, text }
import play.api.libs.json.{ Json, OFormat }

final case class SurveyForm(
  wereYouAble: Int,
  overallRate: Int,
  howEasy: Int,
  journey: String,
  comments: String)

object SurveyForm {
  implicit val format: OFormat[SurveyForm] = Json.format[SurveyForm]

  val surveyForm: Form[SurveyForm] = Form(
    mapping(
      "wereYouAble" -> number(min = 0, max = 1),
      "overallRate" -> number(min = 1, max = 5),
      "howEasy" -> number(min = 1, max = 5),
      "journey" -> text(maxLength = 30),
      "comments" -> text(maxLength = 1200))(SurveyForm.apply)(SurveyForm.unapply))
}
