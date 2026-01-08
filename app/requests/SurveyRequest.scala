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

import model.content.ContentOptions
import paysurvey.audit.AuditOptions
import play.api.mvc.{Request, WrappedRequest}

final case class SurveyRequest[A](
  content:    ContentOptions,
  audit:      AuditOptions,
  origin:     String,
  returnMsg:  String,
  returnHref: String,
  request:    Request[A]
) extends WrappedRequest[A](request)

object SurveyRequest {

  def default[A](implicit request: Request[A]): SurveyRequest[A] = {
    SurveyRequest[A](
      ContentOptions.default,
      AuditOptions.default,
      "Pay-frontend",
      "Skip survey",
      "https://www.gov.uk/government/organisations/hm-revenue-customs",
      request
    )
  }

}
