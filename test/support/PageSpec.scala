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

package support

import org.openqa.selenium.By

import java.time.{Clock, LocalDateTime, ZoneId, ZoneOffset}
import org.openqa.selenium.htmlunit.HtmlUnitDriver
import org.scalatest.concurrent.Eventually.eventually
import org.scalatest.concurrent.IntegrationPatience
import org.scalatest.{OptionValues, TestData}
import org.scalatestplus.selenium.WebBrowser
import org.scalatestplus.play.guice.GuiceOneServerPerTest
import payapi.cardpaymentjourney.model.journey.{Journey, JsdPfSa}
import payapi.corcommon.model.JourneyId
import play.api.Application
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.ws.WSClient
import testdata.{TdAll, TdJourney}
import uk.gov.hmrc.http.{HttpClient, SessionKeys}
import uk.gov.hmrc.http.hooks.HttpHook
import uk.gov.hmrc.play.http.ws.WSHttp

class PageSpec extends UnitSpec with WebBrowser with GuiceOneServerPerTest with WireMockSupport with OptionValues with IntegrationPatience {

  implicit val webDriver: HtmlUnitDriver = new HtmlUnitDriver(true)

  override def newAppForTest(testData: TestData): Application = new GuiceApplicationBuilder()
    .configure(Map[String, Any](
      "application.router" -> "testOnlyDoNotUseInProd.Routes"
    ))
    .build()

  //this HAS to be def or the tests implode - so much time wasted :'(
  def baseUrl = s"http://localhost:$port"

  def frozenTimeString: String = "2027-11-02T16:33:51.880"
  implicit val testClock: Clock = {
    val fixedInstant = LocalDateTime.parse(frozenTimeString).toInstant(ZoneOffset.UTC)
    Clock.fixed(fixedInstant, ZoneId.systemDefault)
  }

  protected trait TestWithJourney {
    val tdJourney: TdJourney[JsdPfSa] = TdAll.lifecyclePfSa.afterSucceedWebPayment.credit
    val journey: Journey[JsdPfSa] = tdJourney.journey
    val journeyId: JourneyId = tdJourney.journey._id
    webDriver.get(s"http://localhost:${port}/payments-survey/test-only/add-to-session/${SessionKeys.sessionId}/${journey.sessionId.value}")
  }
}
