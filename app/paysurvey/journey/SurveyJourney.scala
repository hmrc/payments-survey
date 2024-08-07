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

package paysurvey.journey

import model.content.ContentOptions
import payapi.cardpaymentjourney.model.journey.{Journey, JourneySpecificData}
import paysurvey.audit.AuditOptions

import play.api.libs.json.{Json, OFormat}
import play.api.mvc.Request

import java.time.LocalDateTime

final case class SurveyJourney
  (
    journeyId:  SurveyJourneyId,
    content:    ContentOptions,
    audit:      AuditOptions,
    origin:     String,
    returnMsg:  String,
    returnHref: String,
    createdOn:  LocalDateTime
)

object SurveyJourney {
  @SuppressWarnings(Array("org.wartremover.warts.Any"))
  implicit val format: OFormat[SurveyJourney] = Json.format[SurveyJourney]
}
