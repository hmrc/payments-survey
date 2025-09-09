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

package controllers

import model._
import payapi.cardpaymentjourney.model.journey.JsdPfSa
import paysurvey.journey.{SurveyJourney, SurveyJourneyId}
import paysurvey.journey.ssj.{SsjController, SsjJourneyRequest, SsjResponse}
import play.api.http.Status
import play.api.libs.json.Json
import play.api.mvc.Result
import play.api.test.FakeRequest
import play.api.test.Helpers.{contentAsString, _}
import play.test.Helpers.fakeRequest
import support.AppSpec
import testdata.paysurvey.TdAll.{r, ssjJourneyRequest}

final class SurveyControllerSpec extends AppSpec {
  private val controller = app.injector.instanceOf[SurveyController]
  private val startJourneyController = app.injector.instanceOf[SsjController]

  "survey default should return OK for default" in {
    val putInDb = startJourneyController.startJourney()(r.withBody[SsjJourneyRequest](ssjJourneyRequest))
    val ssjResponse = Json.parse(contentAsString(putInDb)).as[SsjResponse]
    val fakeRequest = FakeRequest("GET", "/")

    val result = controller.surveyDefault(fakeRequest)
    println(contentAsString(result))
    status(result) shouldBe Status.OK
    contentAsString(result).contains("How was our payment service?") shouldBe true
    contentAsString(result).contains("<input class=\"govuk-radios__input\" id=\"wereYouAble\" name=\"wereYouAble\" type=\"radio\" value=\"1\"") shouldBe true

  }

  "survey should render the survey page if the journey is found" in {

    val fakeRequest = FakeRequest("GET", "/")

    val result = controller.surveyJourney(SurveyJourneyId(""))(fakeRequest)

    status(result) shouldBe Status.OK
    contentAsString(result).contains("How was our payment service?") shouldBe true

  }
  "survey should render the survey page if the survey journey is in the db" in {

    val putInDb = startJourneyController.startJourney()(r.withBody[SsjJourneyRequest](ssjJourneyRequest))
    val ssjResponse = Json.parse(contentAsString(putInDb)).as[SsjResponse]
    val fakeRequest = FakeRequest("GET", "/")
    val result = controller.surveyJourney(ssjResponse.journeyId)(fakeRequest)
    status(result) shouldBe Status.OK
    contentAsString(result).contains("How was our payment service?") shouldBe true
    contentAsString(result).contains("returnHref") shouldBe true
    contentAsString(result).contains("returnMsg") shouldBe true
  }

  "survey should render the survey page if there is no Journey ID in the request" in {
    val result = controller.surveyJourney(SurveyJourneyId(""))(FakeRequest("GET", "/"))

    status(result) shouldBe Status.OK
    contentAsString(result).contains("How was our payment service?") shouldBe true

  }

  "surveyThanks should render the survey thanks page if there is no session ID in the session" in {
    val result = controller.showSurveyThanks(SurveyJourneyId(""))(FakeRequest("GET", "/"))

    status(result) shouldBe Status.OK
    contentAsString(result).contains("Thank you") shouldBe true

  }
}
