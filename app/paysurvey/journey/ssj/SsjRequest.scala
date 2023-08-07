/*
 * Copyright 2023 HM Revenue & Customs
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

package paysurvey.journey.ssj

import model.content.ContentOptions
import paysurvey.audit.AuditOptions
import paysurvey.journey.{SurveyJourneyId, SurveyJourney}
import play.api.libs.json.Json
import uk.gov.hmrc.http.SessionId

import java.time.LocalDateTime

final case class SsjRequest
  (
    audit: AuditOptions
)

object SsjRequest {
  implicit val format = Json.format[SsjRequest]
}

final case class SsjJourneyRequest
  (
    origin:         String,
    returnMsg:      String,
    returnHref:     String,
    auditName:      String,
    audit:          AuditOptions,
    contentOptions: ContentOptions
) {
  def toSurveyJourney(
      journeyId:      SurveyJourneyId,
      createdOn:      LocalDateTime,
      contentOptions: ContentOptions
  ): SurveyJourney = {

    SurveyJourney(
      journeyId,
      contentOptions,
      audit,
      origin,
      returnMsg,
      returnHref,
      createdOn
    )
  }

}

object SsjJourneyRequest {
  implicit val format = Json.format[SsjJourneyRequest]
}

