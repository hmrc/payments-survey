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

package support

import java.time.{ Clock, LocalDateTime, ZoneId, ZoneOffset }

import org.openqa.selenium.htmlunit.HtmlUnitDriver
import org.scalatest.TestData
import org.scalatest.selenium.WebBrowser
import org.scalatestplus.play.guice.GuiceOneServerPerTest
import play.api.Application
import play.api.inject.guice.GuiceApplicationBuilder
import testdata.TestData
import uk.gov.hmrc.http.SessionKeys

trait PageSpec extends UnitSpec with GuiceOneServerPerTest with WireMockSupport with WebBrowser {
  //  protected implicit def executionContext: ExecutionContext = app.injector.instanceOf[ExecutionContext]
  //  protected implicit def hc: HeaderCarrier = HeaderCarrier()

  implicit val webDriver: HtmlUnitDriver = new HtmlUnitDriver(true)

  override def newAppForTest(testData: TestData): Application = new GuiceApplicationBuilder()
    .configure(Map[String, Any](
      "microservice.services.pay-api.port" -> WireMockSupport.port))
    .build()

  def frozenTimeString: String = "2027-11-02T16:33:51.880"
  implicit val testClock: Clock = {
    val fixedInstant = LocalDateTime.parse(frozenTimeString).toInstant(ZoneOffset.UTC)
    Clock.fixed(fixedInstant, ZoneId.systemDefault)
  }

  protected trait TestWithSession {
    webDriver.get(s"http://localhost:${port}/pay/test-only/add-to-session/${SessionKeys.sessionId}/${TestData.testSessionId.value}")
  }
}
