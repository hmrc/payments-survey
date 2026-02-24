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

package config

import play.api.Configuration

import javax.inject.{Inject, Singleton}
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig

import scala.util.Try

@Singleton
class AppConfig @Inject() (config: Configuration, servicesConfig: ServicesConfig) {
  private def loadConfig(key: String) = servicesConfig.getString(key)

  private val contactHost          = servicesConfig.getConfString(s"contact-frontend.host", "")
  val platformHost: Option[String] = config.getOptional[String]("platform.frontend.host")

  val paymentSurvey: String = platformHost.getOrElse(config.get[String]("frontendBaseUrl"))

  private val contactFormServiceIdentifier     = "MyService"
  lazy val optimizelyProjectId: Option[String] = Try(servicesConfig.getString(s"optimizely.projectId")).toOption

  lazy val frontendBaseUrl: String = s"$paymentSurvey/payments-survey"

  lazy val payFrontendBaseUrl: String = s"${servicesConfig.getString("payFrontendBaseUrl")}/pay"

  val ggBaseUrl: String = loadConfig("ggBaseUrl") + "/gg"

  val basGatewayBaseUrl: String = platformHost.getOrElse(config.get[String]("basGatewayBaseUrl"))
  val signOutUrl: String        = s"$basGatewayBaseUrl/bas-gateway/sign-out-without-state"

  lazy val analyticsToken: String            = loadConfig(s"google-analytics.token")
  lazy val analyticsHost: String             = loadConfig(s"google-analytics.host")
  lazy val reportAProblemPartialUrl          = s"$contactHost/contact/problem_reports_ajax?service=$contactFormServiceIdentifier"
  lazy val reportAProblemNonJSUrl            = s"$contactHost/contact/problem_reports_nonjs?service=$contactFormServiceIdentifier"
  lazy val accessibilityStatementUrl: String =
    s"${servicesConfig.getString("payFrontendBaseUrl")}/accessibility-statement/pay?referrerUrl=https%3A%2F%2Fwww.tax.service.gov.uk%2Fpay-frontend"
  val privacyNoticeUrl: String               = loadConfig("govUkUrls.privacyNoticeUrl")
  val paymentSupportUrl: String              = loadConfig("govUkUrls.paymentSupportUrl")
  val cookiesUrl: String                     = loadConfig("govUkUrls.cookiesUrl")
  val termsAndConditionsUrl: String          = loadConfig("govUkUrls.termsAndConditionsUrl")
  val helpUsingGovUkUrl: String              = loadConfig("govUkUrls.helpUsingGovUkUrl")
}
