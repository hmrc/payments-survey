import play.core.PlayVersion.current
import sbt._

object AppDependencies {

  private val catsVersion = "2.1.1"
  private val payApiVersion = "[1.25.0,)"

  val compile = Seq(
    "uk.gov.hmrc"             %% "bootstrap-frontend-play-27"       % "3.4.0",
    "uk.gov.hmrc"             %% "govuk-template"                   % "5.65.0-play-26",
    "uk.gov.hmrc"             %% "play-ui"                          % "9.1.0-play-26",
    "uk.gov.hmrc"             %% "pay-api-cor-card-payment-journey" % payApiVersion,
    "com.beachape"            %% "enumeratum" % "1.6.1",
    "org.typelevel"           %% "cats-core" % catsVersion,
    "org.typelevel"           %% "cats-kernel" % catsVersion,
    "org.typelevel"           %% "cats-macros" % catsVersion
  )

  val test = Seq(
    "org.scalatest"           %% "scalatest"                % "3.0.8"                 % Test,
    "com.typesafe.play"       %% "play-test"                % current                 % Test,
    "org.pegdown"             %  "pegdown"                  % "1.6.0"                 % Test,
    "org.scalatestplus.play"  %% "scalatestplus-play"       % "3.1.3"                 % Test,
    "com.github.tomakehurst"  %  "wiremock-standalone"      % "2.27.1"                % Test,
    "uk.gov.hmrc"             %% "pay-api-cor-card-payment-journey-test-data" % payApiVersion % Test
  )

}