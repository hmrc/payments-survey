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

package pagespec

import play.api.libs.ws.writeableOf_JsValue
import org.openqa.selenium.By
import org.scalatestplus.selenium.WebBrowser
import paysurvey.journey.ssj.SsjResponse
import play.api.libs.json.Json
import support.AppSpec
import testdata.paysurvey.TdAll.ssjJourneyRequest
import uk.gov.hmrc.http.HttpReads.Implicits._
import uk.gov.hmrc.http.{HeaderCarrier, HttpResponse, StringContextOps}

class SurveyPageSpec extends AppSpec with WebBrowser {
  def baseUrl = s"http://localhost:$port"
  def pagePath(id: String) = baseUrl + s"/payments-survey/v2/survey/$id"
  def surveyThanksPath(id: String) = s"/payments-survey/survey-thanks/$id"
  protected trait TestWithJourney {
    implicit val hc: HeaderCarrier = HeaderCarrier()
    val ssjUrl = url"http://localhost:${port}/payments-survey/journey/start"

    val response: HttpResponse =
      testHttpClient
        .post(ssjUrl)
        .withBody(Json.toJson(ssjJourneyRequest))
        .execute[HttpResponse]
        .futureValue
    val ssjResponse = response.json.as[SsjResponse]

  }
  "should render correctly" in new TestWithJourney {

    goTo(pagePath(ssjResponse.journeyId.value))

    cssSelector(".govuk-header__service-name")
      .element.text shouldEqual ssjJourneyRequest.contentOptions.title.englishValue

    pageTitle shouldBe "How was our payment service? - Pay your tax - GOV.UK"

    id("were-you-able-label").element.text shouldBe "1. Were you able to do what you needed to do today?"
    cssSelector("#were-you-able-q > div > fieldset > div > div:nth-child(1) > label")
      .element.text shouldBe "Yes"
    cssSelector("#were-you-able-q > div > fieldset > div > div:nth-child(2) > label")
      .element.text shouldBe "No"

    id("how-easy-label")
      .element.text shouldBe "2. How easy was it for you to do what you needed to do today?"
    cssSelector("#how-easy-q > div > fieldset > div > div:nth-child(1) > label")
      .element.text shouldBe "Very easy"
    cssSelector("#how-easy-q > div > fieldset > div > div:nth-child(2) > label")
      .element.text shouldBe "Easy"
    cssSelector("#how-easy-q > div > fieldset > div > div:nth-child(3) > label")
      .element.text shouldBe "Neither easy or difficult"
    cssSelector("#how-easy-q > div > fieldset > div > div:nth-child(4) > label")
      .element.text shouldBe "Difficult"
    cssSelector("#how-easy-q > div > fieldset > div > div:nth-child(5) > label")
      .element.text shouldBe "Very difficult"

    id("why-score-label").element.text shouldBe "2b. Why did you give this score?"
    id("comments").element.attribute("value") shouldBe Some("")

    id("overall-label")
      .element.text shouldBe "3. Overall, how did you feel about the service you received today?"
    cssSelector("#overall-q > div > fieldset > div > div:nth-child(1) > label")
      .element.text shouldBe "Very satisfied"
    cssSelector("#overall-q > div > fieldset > div > div:nth-child(2) > label")
      .element.text shouldBe "Satisfied"
    cssSelector("#overall-q > div > fieldset > div > div:nth-child(3) > label")
      .element.text shouldBe "Neither satisfied or dissatisfied"
    cssSelector("#overall-q > div > fieldset > div > div:nth-child(4) > label")
      .element.text shouldBe "Dissatisfied"
    cssSelector("#overall-q > div > fieldset > div > div:nth-child(5) > label")
      .element.text shouldBe "Very dissatisfied"

    id("thank-you-header").element.text shouldBe "Thank you for your feedback"
    id("thank-you-message").element.text shouldBe "We will use your feedback to make our services better."
    id("continue-button").element.text shouldBe "Send feedback"

  }

  "should show no errors if the user hasn't clicked submit" in new TestWithJourney {
    goTo(pagePath(ssjResponse.journeyId.value))

    webDriver.findElements(By.className("govuk-error-summary__title")).isEmpty shouldBe true
    webDriver.findElements(By.id("wereYouAble-error")).isEmpty shouldBe true
    webDriver.findElements(By.id("howEasy-error")).isEmpty shouldBe true
    webDriver.findElements(By.id("overallRate-error")).isEmpty shouldBe true
  }

  "should show an error if the user tries to submit without filling in any fields" in new TestWithJourney {
    goTo(pagePath(ssjResponse.journeyId.value))
    click.on("continue-button")
    currentUrl shouldBe pagePath(ssjResponse.journeyId.value)
    cssSelector(".govuk-error-summary__title").element.text shouldBe "There's a problem"
    id("wereYouAble-error").element.text shouldBe "Error: This field is required"
    id("howEasy-error").element.text shouldBe "Error: This field is required"
    id("overallRate-error").element.text shouldBe "Error: This field is required"
  }

  "should proceed to the survey thanks page if the user submits after filling in all necessary fields" in new TestWithJourney {

    goTo(pagePath(ssjResponse.journeyId.value))
    click.on("wereYouAble")
    click.on("howEasy")
    click.on("overallRate")
    click.on("continue-button")
    currentUrl shouldBe (baseUrl + surveyThanksPath(ssjResponse.journeyId.value))
  }

  "ensure can switch to Welsh and back" in new TestWithJourney {
    goTo(pagePath(ssjResponse.journeyId.value))
    click.on(xpath("/html/body/div/div/nav/ul/li[2]/a/span[2]"))
    pageTitle shouldBe "Sut oedd eich gwasanaeth talu? - Talu treth - GOV.UK"
    click.on(xpath("/html/body/div/div/nav/ul/li[1]/a/span[2]"))
    pageTitle shouldBe "How was our payment service? - Pay your tax - GOV.UK"
  }
}
