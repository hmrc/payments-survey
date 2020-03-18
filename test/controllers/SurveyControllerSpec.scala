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

package controllers

import mock.PayApiStubGetJourney
import payapi.corcommon.model.JourneyId
import play.api.http.Status
import play.api.mvc.{ AnyContentAsEmpty, Result }
import play.api.test.FakeRequest
import support.AppSpec
import play.api.test.Helpers._
import testdata.TestData
import uk.gov.hmrc.http.SessionKeys

import scala.concurrent.Future

final class SurveyControllerSpec extends AppSpec {
  private val controller = app.injector.instanceOf[SurveyController]

  "route should send the user to pay-frontend CATTP if there is no Journey ID in the session" in {
    val fakeRequest = FakeRequest("GET", "/")

    val result = controller.route()(fakeRequest)

    status(result) shouldBe Status.SEE_OTHER
    redirectLocation(result) shouldBe Some("http://localhost:9056/pay")
  }

  "route should send the user to the main survey page if there is a Journey ID in the session" in {
    val fakeRequest = FakeRequest("GET", "/")

    val result = controller.route()(fakeRequest.withSession(("journeyId", "123456")))

    status(result) shouldBe Status.SEE_OTHER
    redirectLocation(result) shouldBe Some("/payments-survey/123456/survey")
  }

  "survey should return 404 to the user if pay-api is not responsive" in {
    val fakeRequest = FakeRequest("GET", "/")

    val result = controller.survey(TestData.testJourneyId)(fakeRequest)

    status(result) shouldBe Status.NOT_FOUND
  }

  "survey should render the survey page if pay-api is responsive" in {
    PayApiStubGetJourney.getJourney2xx(TestData.testJourney)

    val fakeRequest = FakeRequest("GET", "/")

    val result = controller.survey(TestData.testJourneyId)(fakeRequest.withSession((SessionKeys.sessionId, TestData.testSessionId.value)))

    status(result) shouldBe Status.OK
    contentAsString(result).contains("How was our payment service?") shouldBe true
  }

  "surveyThanks should return 404 to the user if pay-api is not responsive" in {
    val fakeRequest = FakeRequest("GET", "/")

    val result = controller.surveyThanks(TestData.testJourneyId)(fakeRequest)

    status(result) shouldBe Status.NOT_FOUND
  }

  "surveyThanks should render the survey page if pay-api is responsive" in {
    PayApiStubGetJourney.getJourney2xx(TestData.testJourney)

    val fakeRequest = FakeRequest("GET", "/")

    val result = controller.surveyThanks(TestData.testJourneyId)(fakeRequest.withSession((SessionKeys.sessionId, TestData.testSessionId.value)))

    status(result) shouldBe Status.OK
    contentAsString(result).contains("Thank you") shouldBe true
  }
}
