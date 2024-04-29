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

import paysurvey.journey.ssj.{SsjResponse, SsjService}
import support.AppSpec
import testdata.paysurvey.TdAll._
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.Future

class JourneyServiceSpec extends AppSpec {

  private val service = app.injector.instanceOf[JourneyService]

  private val ssjService = app.injector.instanceOf[SsjService]

  def startJourney(): Future[SsjResponse] = {
    implicit val hc: HeaderCarrier = HeaderCarrier()

    ssjService.startJourney(ssjJourneyRequest)
  }

  "return None when no journey for JourneyId" in {
    service.findLatestByJourneyId(journeyId).futureValue shouldBe None
  }

  "return journey for JourneyId if there is a journey" in {
    val ssjResponse = startJourney().futureValue

    val j = {
      val maybeJ = service.findLatestByJourneyId(ssjResponse.journeyId).futureValue
      maybeJ.isDefined shouldBe true
      maybeJ.get
    }

    j.journeyId shouldBe ssjResponse.journeyId
  }

  "return latest journey for sessionId if there is multiple journeys" in {
    val ssjResponse = startJourney().futureValue
    val ssjResponse2 = startJourney().futureValue

    val j = {
      val maybeJ = service.findLatestByJourneyId(ssjResponse2.journeyId).futureValue
      maybeJ.isDefined shouldBe true
      maybeJ.get
    }

    j.journeyId shouldBe ssjResponse2.journeyId
  }

}
