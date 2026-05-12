/*
 * Copyright 2026 HM Revenue & Customs
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

package paysurvey.audit

import play.api.libs.json.{Json, Writes}

final case class AuditDetail(
  wereYouAble:  String,
  overallRate:  String,
  howEasy:      String,
  userType:     String,
  comments:     Option[String],
  journey:      Option[String],
  orderId:      Option[String],
  liability:    Option[String],
  surveySource: Option[String],
  paymentId:    Option[String],
  origin:       Option[String]
)

object AuditDetail:
  def apply(
    auditOptions: AuditOptions,
    wereYouAble:  String,
    overallRate:  String,
    howEasy:      String,
    comments:     Option[String]
  ): AuditDetail =
    AuditDetail(
      wereYouAble = wereYouAble,
      overallRate = overallRate,
      howEasy = howEasy,
      userType = auditOptions.userType,
      comments = comments,
      journey = auditOptions.journey,
      orderId = auditOptions.orderId,
      liability = auditOptions.liability,
      surveySource = auditOptions.surveySource,
      paymentId = auditOptions.paymentId,
      origin = auditOptions.origin
    )

  given Writes[AuditDetail] = Json.writes[AuditDetail]
