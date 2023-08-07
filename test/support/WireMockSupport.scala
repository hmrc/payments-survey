/*
 * Copyright 2023 HM Revenue & Customs
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

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach, Suite}
import play.api.Logger

trait WireMockSupport extends BeforeAndAfterAll with BeforeAndAfterEach {
  self: Suite =>
  import WireMockSupport._

  implicit val wireMockServer: WireMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig().port(WireMockSupport.port))

  WireMock.configureFor(port)

  override protected def beforeAll(): Unit = wireMockServer.start()

  override protected def afterAll(): Unit = {
    Logger(this.getClass).info("Stopping wire mock server ...")
    wireMockServer.stop()
    Logger(this.getClass).info("Stopping wire mock server - done")
  }

  override def beforeEach() = {
    Logger(this.getClass).info("Resetting wire mock server ...")
    WireMock.reset()
    Logger(this.getClass).info("Resetting wire mock server - done")
  }

}

object WireMockSupport {
  val port = 11111
}
