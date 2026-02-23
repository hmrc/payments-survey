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
import paysurvey.audit.AuditOptions
import play.api.libs.json.*

import java.time.{Instant, LocalDateTime, ZoneOffset}

final case class SurveyJourney(
  journeyId:  SurveyJourneyId,
  content:    ContentOptions,
  audit:      AuditOptions,
  origin:     String,
  returnMsg:  String,
  returnHref: String,
  createdOn:  LocalDateTime
)

object SurveyJourney {

  final val localDateTimeReads: Reads[LocalDateTime] =
    Reads
      .at[String](__ \ "$date" \ "$numberLong")
      .map(dateTime => Instant.ofEpochMilli(dateTime.toLong).atZone(ZoneOffset.UTC).toLocalDateTime)

  final val localDateTimeWrites: Writes[LocalDateTime] =
    Writes
      .at[String](__ \ "$date" \ "$numberLong")
      .contramap(_.toInstant(ZoneOffset.UTC).toEpochMilli.toString)

  implicit val localDateTimeFormat: Format[LocalDateTime] = Format(localDateTimeReads, localDateTimeWrites)

  @SuppressWarnings(Array("org.wartremover.warts.Any"))
  implicit val format: OFormat[SurveyJourney] = Json.format[SurveyJourney]
}
