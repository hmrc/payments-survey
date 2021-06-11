/*
 * Copyright 2021 HM Revenue & Customs
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

import model._
import payapi.cardpaymentjourney.model.journey.JsdPfSa
import play.api.http.Status
import play.api.test.FakeRequest
import play.api.test.Helpers._
import stubs.PayApiStubFindJourneyBySessionId
import support.AppSpec
import testdata.{TdAll, TdJourney}
import uk.gov.hmrc.http.SessionKeys

final class SurveyControllerSpec extends AppSpec {
  private val controller = app.injector.instanceOf[SurveyController]

  "survey should render the survey page if pay-api is responsive" in {
    val tdJourney = TdAll.lifecyclePfSa.afterSucceedWebPayment.credit
    val journey = tdJourney.journey
    val sessionId = tdJourney.journey.sessionId.value
    PayApiStubFindJourneyBySessionId.findBySessionId2xx(tdJourney)

    val fakeRequest = FakeRequest("GET", "/")

    val result = controller.survey()(fakeRequest.withSession((SessionKeys.sessionId, sessionId.value)))

    status(result) shouldBe Status.OK
    contentAsString(result).contains("How was our payment service?") shouldBe true
    contentAsString(result).contains(journey.contentOptions.title.englishValue) shouldBe true

    PayApiStubFindJourneyBySessionId.findBySessionIdVerify()
  }

  "surveyThanks should render the survey thanks page if pay-api is responsive" in {
    val tdJourney: TdJourney[JsdPfSa] = TdAll.lifecyclePfSa.afterSucceedWebPayment.credit
    val journey = tdJourney.journey
    PayApiStubFindJourneyBySessionId.findBySessionId2xx(tdJourney)

    val fakeRequest = FakeRequest("GET", "/")

    val result = controller.showSurveyThanks()(fakeRequest.withSession((SessionKeys.sessionId, journey.sessionId.value.value)))

    status(result) shouldBe Status.OK
    contentAsString(result).contains("Thank you") shouldBe true
    contentAsString(result).contains(journey.contentOptions.title.englishValue) shouldBe true

    PayApiStubFindJourneyBySessionId.findBySessionIdVerify()
  }

  "survey should render the survey page if pay-api is responsive but returns None" in {
    PayApiStubFindJourneyBySessionId.findBySessionId404()

    val result = controller.survey()(FakeRequest("GET", "/").withSession((SessionKeys.sessionId, "123")))

    status(result) shouldBe Status.OK
    contentAsString(result).contains("How was our payment service?") shouldBe true

    PayApiStubFindJourneyBySessionId.findBySessionIdVerify()
  }

  "surveyThanks should render the survey thanks page if pay-api is responsive but returns None" in {
    PayApiStubFindJourneyBySessionId.findBySessionId404()

    val result = controller.showSurveyThanks()(FakeRequest("GET", "/").withSession((SessionKeys.sessionId, "123")))

    status(result) shouldBe Status.OK
    contentAsString(result).contains("Thank you") shouldBe true

    PayApiStubFindJourneyBySessionId.findBySessionIdVerify()
  }

  "survey should render the survey page if there is no session ID in the session" in {
    val result = controller.survey()(FakeRequest("GET", "/"))

    status(result) shouldBe Status.OK
    contentAsString(result).contains("How was our payment service?") shouldBe true

    PayApiStubFindJourneyBySessionId.findBySessionIdVerifyNot()
  }

  "surveyThanks should render the survey thanks page if there is no session ID in the session" in {
    val result = controller.showSurveyThanks()(FakeRequest("GET", "/"))

    status(result) shouldBe Status.OK
    contentAsString(result).contains("Thank you") shouldBe true

    PayApiStubFindJourneyBySessionId.findBySessionIdVerifyNot()
  }
}
