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

package paysurvey.journey.ssj

import play.api.libs.json.Json
import support.AppSpec
import testdata.paysurvey.TdAll.ssjJourneyRequest
import uk.gov.hmrc.http.{HeaderCarrier, HttpResponse, StringContextOps}
import uk.gov.hmrc.http.HttpReads.Implicits._

class StartJourneySpec extends AppSpec {

  implicit val hc: HeaderCarrier = HeaderCarrier()

  "start survey journey should " in {
    val ssjUrl = url"http://localhost:${port}/payments-survey/journey/start"

    val response: HttpResponse =
      testHttpClient
        .post(ssjUrl)
        .withBody(Json.toJson(ssjJourneyRequest))
        .execute[HttpResponse]
        .futureValue
    val ssjResponse = response.json.as[SsjResponse]

    ssjResponse.nextUrl.value shouldBe s"http://localhost:9966/payments-survey/v2/survey/${ssjResponse.journeyId.value}"
  }

}
