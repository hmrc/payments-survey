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

package testdata.paysurvey

import model.content.ContentOptions
import paysurvey.audit.AuditOptions
import paysurvey.journey.ssj.{SsjJourneyRequest, SsjRequest}
import play.api.mvc.AnyContentAsEmpty
import play.api.test.FakeRequest
import requests.SurveyRequest

trait TdSsjRequest extends TdBase {

  implicit val r: FakeRequest[AnyContentAsEmpty.type] = FakeRequest("GET", "/")

  val auditOptions: AuditOptions = AuditOptions(
    userType = "IsLoggedIn"
  )

  val ssjRequest: SsjRequest = SsjRequest(
    auditOptions
  )
  val ssjJourneyRequest: SsjJourneyRequest = SsjJourneyRequest(
    "lala",
    "returnMsg",
    "returnHref",
    "auditname",
    auditOptions,
    ContentOptions.default
  )

  val surveyRequest: SurveyRequest[SsjRequest] = SurveyRequest(
    ContentOptions.default,
    AuditOptions.default,
    "lala",
    "backLinkMessage",
    "backLinkHref",
    r
      .withBody(ssjRequest)
  )

}
