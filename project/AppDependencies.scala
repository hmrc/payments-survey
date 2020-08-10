import play.core.PlayVersion.current
import sbt._

object AppDependencies {

  private val catsVersion = "1.1.0"
  private val payApiVersion = "[0.387.0,)"

  val compile = Seq(
    "uk.gov.hmrc"             %% "govuk-template"                   % "5.36.0-play-26",
    "uk.gov.hmrc"             %% "play-ui"                          % "7.40.0-play-26",
    "uk.gov.hmrc"             %% "bootstrap-play-26"                % "0.42.0",
    "uk.gov.hmrc"             %% "pay-api-cor-card-payment-journey" % payApiVersion,
    "com.beachape"            %% "enumeratum" % "1.5.13",
    "org.typelevel"           %% "cats-core" % catsVersion,
    "org.typelevel"           %% "cats-kernel" % catsVersion,
    "org.typelevel"           %% "cats-macros" % catsVersion
  )

  val test = Seq(
    "org.scalatest"           %% "scalatest"                % "3.0.8"                 % Test,
    "com.typesafe.play"       %% "play-test"                % current                 % Test,
    "org.pegdown"             %  "pegdown"                  % "1.6.0"                 % Test,
    "org.scalatestplus.play"  %% "scalatestplus-play"       % "3.1.2"                 % Test,
    "com.github.tomakehurst"  %  "wiremock-standalone"      % "2.12.0"                % Test,
    "uk.gov.hmrc"             %% "pay-api-cor-card-payment-journey-test-data" % payApiVersion % Test
  )

}
