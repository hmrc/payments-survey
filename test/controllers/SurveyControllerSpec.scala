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

import org.jsoup.Jsoup
import org.jsoup.select.Elements
import paysurvey.journey.SurveyJourneyId
import paysurvey.journey.ssj.{SsjController, SsjJourneyRequest, SsjResponse}
import play.api.http.Status
import play.api.libs.json.{JsObject, Json}
import play.api.test.FakeRequest
import play.api.test.Helpers.{contentAsString, *}
import stubs.AuditConnectorStub
import support.AppSpec
import testdata.paysurvey.TdAll.{auditTestSsjJourneyRequest, r, ssjJourneyRequest}

import scala.jdk.CollectionConverters.CollectionHasAsScala

final class SurveyControllerSpec extends AppSpec {

  override protected lazy val configOverrides: Map[String, Any] = Map[String, Any](
    "auditing.enabled" -> true
  )

  private val surveyController = app.injector.instanceOf[SurveyController]
  private val ssjController    = app.injector.instanceOf[SsjController]

  "survey default should return OK for default" in {
    ssjController.startJourney()(r.withBody[SsjJourneyRequest](ssjJourneyRequest))
    val fakeRequest = FakeRequest("GET", "/")

    val result = surveyController.surveyDefault(fakeRequest)
    status(result) shouldBe Status.OK

    val doc        = Jsoup.parse(contentAsString(result))
    doc
      .select("p.govuk-body")
      .text shouldBe "We use your feedback to improve our services. To better understand it, we may link your feedback to other information we hold about you, like gender and age. See the HMRC Privacy Notice for details about how we collect, use, protect and secure your personal information. The survey takes about 1 minute to complete. There are 4 questions and they are all optional. We will use your feedback to make our services better."
    doc.select("h1.govuk-heading-xl").text shouldBe "How was our payment service?"
    val formGroups = doc.select("div.govuk-form-group").asScala.toList

    def analyseRadios(radios: Elements) = {
      radios.select("div.govuk-radios__item").asScala.toList.map { r =>
        val (input, label) = (r.select("input.govuk-radios__input"), r.select("label.govuk-radios__label"))
        (input.attr("name"), input.attr("value"), label.text)
      }
    }

    val yesNoRadios = formGroups.head.select("fieldset.govuk-fieldset > div.govuk-radios")
    analyseRadios(yesNoRadios) shouldBe List(("wereYouAble", "1", "Yes"), ("wereYouAble", "0", "No"))

    val difficultyRadios = formGroups(1).select("fieldset.govuk-fieldset > div.govuk-radios")
    analyseRadios(difficultyRadios) shouldBe List(
      ("howEasy", "5", "Very easy"),
      ("howEasy", "4", "Easy"),
      ("howEasy", "3", "Neither easy or difficult"),
      ("howEasy", "2", "Difficult"),
      ("howEasy", "1", "Very difficult")
    )

    val satisfiedRadios = formGroups(3).select("fieldset.govuk-fieldset > div.govuk-radios")
    analyseRadios(satisfiedRadios) shouldBe List(
      ("overallRate", "5", "Very satisfied"),
      ("overallRate", "4", "Satisfied"),
      ("overallRate", "3", "Neither satisfied or dissatisfied"),
      ("overallRate", "2", "Dissatisfied"),
      ("overallRate", "1", "Very dissatisfied")
    )

    val h2s = doc.select("h2.govuk-heading-m").asScala.toList
    h2s.map(_.text) shouldBe List(
      "1. Were you able to do what you needed to do today?",
      "2. How easy was it for you to do what you needed to do today?",
      "2b. Why did you give this score?",
      "3. Overall, how did you feel about the service you received today?"
    )
    doc.select("h3.govuk-heading-m").text shouldBe "Thank you for your feedback"
  }

  "survey should render the survey page if the journey is found" in {

    val fakeRequest = FakeRequest("GET", "/")

    val result = surveyController.surveyJourney(SurveyJourneyId(""))(fakeRequest)

    status(result) shouldBe Status.OK
    contentAsString(result).contains("How was our payment service?") shouldBe true

  }

  "survey should render the survey page if the survey journey is in the db" in {

    val putInDb     = ssjController.startJourney()(r.withBody[SsjJourneyRequest](ssjJourneyRequest))
    val ssjResponse = Json.parse(contentAsString(putInDb)).as[SsjResponse]
    val fakeRequest = FakeRequest("GET", "/")
    val result      = surveyController.surveyJourney(ssjResponse.journeyId)(fakeRequest)
    status(result) shouldBe Status.OK
    contentAsString(result).contains("How was our payment service?") shouldBe true
    contentAsString(result).contains("returnHref") shouldBe true
    contentAsString(result).contains("returnMsg") shouldBe true
  }

  "survey should render the survey page if there is no Journey ID in the request" in {
    val result = surveyController.surveyJourney(SurveyJourneyId(""))(FakeRequest("GET", "/"))

    status(result) shouldBe Status.OK
    contentAsString(result).contains("How was our payment service?") shouldBe true

  }

  "surveyThanks should render the survey thanks page if there is no session ID in the session" in {
    val result = surveyController.showSurveyThanks(SurveyJourneyId(""))(FakeRequest("GET", "/"))

    status(result) shouldBe Status.OK
    contentAsString(result).contains("Thank you") shouldBe true

  }

  "submitSurvey" - {
    "should redirect to the thanks page and trigger explicit audit event" in {
      val putInDb     = ssjController.startJourney()(r.withBody[SsjJourneyRequest](auditTestSsjJourneyRequest))
      val ssjResponse = Json.parse(contentAsString(putInDb)).as[SsjResponse]
      val result      = surveyController.submitSurvey(ssjResponse.journeyId)(
        FakeRequest("POST", s"/survey/${ssjResponse.journeyId.value}").withFormUrlEncodedBody(
          "journey"     -> "Created",
          "wereYouAble" -> "1",
          "howEasy"     -> "5",
          "comments"    -> "asdfg",
          "overallRate" -> "5"
        )
      )
      redirectLocation(result) shouldBe Some(s"/payments-survey/survey-thanks/${ssjResponse.journeyId.value}")
      result.futureValue.header.status shouldBe 303
      AuditConnectorStub.verifyEventAudited(
        auditType = "Questionnaire",
        auditEvent = Json
          .parse(
            // language=JSON
            """
              |{
              |   "wereYouAble" : "1",
              |   "overallRate" : "5",
              |   "howEasy" : "5",
              |   "userType" : "IsLoggedIn",
              |   "comments" : "asdfg",
              |   "journey" : "journey",
              |   "orderId" : "orderId",
              |   "liability" : "liability",
              |   "surveySource" : "surveySource",
              |   "paymentId" : "paymentId",
              |   "origin" : "lala"
              |}
              |""".stripMargin
          )
          .as[JsObject]
      )
    }
  }
}
