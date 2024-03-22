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

import paysurvey.journey.{JourneyService, SurveyJourney}
import play.api.libs.json.Json
import play.api.test.Helpers.{contentAsString, defaultAwaitTimeout, status}
import support.AppSpec
import testdata.paysurvey.TdAll._

class SsjControllerSpec extends AppSpec {

  private val controller = app.injector.instanceOf[SsjController]

  private val journeyService = app.injector.instanceOf[JourneyService]

  "start survey journey v2 should " in {
    val result = controller.startJourney()(r.withBody[SsjJourneyRequest](ssjJourneyRequest))

    val responseBody = Json.parse(contentAsString(result)).as[SsjResponse]
    responseBody.nextUrl.value shouldBe s"http://localhost:9966/payments-survey/v2/survey/${responseBody.journeyId.value}"

    val j: SurveyJourney = {
      val maybeJ = journeyService.findLatestByJourneyId(responseBody.journeyId).futureValue
      maybeJ.isDefined shouldBe true
      maybeJ.get
    }

    j.journeyId shouldBe responseBody.journeyId
  }

}
