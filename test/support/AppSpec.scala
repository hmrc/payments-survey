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

import java.time.{Clock, LocalDateTime, ZoneId, ZoneOffset}
import org.openqa.selenium.htmlunit.HtmlUnitDriver
import com.gargoylesoftware.htmlunit.BrowserVersion
import org.scalatest.{AppendedClues, OptionValues}
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Millis, Seconds, Span}
import org.scalatestplus.play.guice.{GuiceOneServerPerSuite}
import play.api.Application
import play.api.inject.guice.{GuiceApplicationBuilder}
import uk.gov.hmrc.http.client.{HttpClientV2}

import scala.concurrent.ExecutionContext

trait AppSpec
  extends UnitSpec
  with GuiceOneServerPerSuite
  with WireMockSupport
  with ScalaFutures
  with AppendedClues
  with MongoTestSupport
  with OptionValues {
  implicit val webDriver: HtmlUnitDriver = new HtmlUnitDriver(BrowserVersion.CHROME)

  protected def configMap: Map[String, Any] =
    Map[String, Any](
      "microservice.services.pay-api.port" -> WireMockSupport.port,
      "mongodb.uri" -> mongoUri
    )

  override def fakeApplication(): Application = new GuiceApplicationBuilder()
    .configure(configMap)
    .build()

  implicit lazy val ec: ExecutionContext = app.injector.instanceOf[ExecutionContext]

  lazy val testHttpClient: HttpClientV2 = app.injector.instanceOf[HttpClientV2]

  override implicit val patienceConfig: PatienceConfig = PatienceConfig(
    timeout  = scaled(Span(3, Seconds)),
    interval = scaled(Span(300, Millis))
  )

  def frozenTimeString: String = "2027-11-02T16:33:51.880"
  implicit val testClock: Clock = {
    val fixedInstant = LocalDateTime.parse(frozenTimeString).toInstant(ZoneOffset.UTC)
    Clock.fixed(fixedInstant, ZoneId.systemDefault)
  }
}
