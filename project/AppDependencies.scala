import play.core.PlayVersion.current
import sbt._

object AppDependencies {

  private val catsVersion = "2.1.1"
  private val payApiVersion = "[1.25.0,)"

  val compile = Seq(
    "uk.gov.hmrc"             %% "bootstrap-frontend-play-28"       % "5.2.0",
    "uk.gov.hmrc"             %% "play-frontend-govuk"              % "0.71.0-play-28",
    "uk.gov.hmrc"             %% "play-frontend-hmrc"               % "0.60.0-play-28",
    "uk.gov.hmrc"             %% "pay-api-cor-card-payment-journey" % payApiVersion,
    "com.beachape"            %% "enumeratum"                       % "1.6.1",
    "org.typelevel"           %% "cats-core"                        % catsVersion,
    "org.typelevel"           %% "cats-kernel"                      % catsVersion,
    "org.typelevel"           %% "cats-macros"                      % catsVersion
  )

  val test = Seq(
    "org.scalatest"           %% "scalatest"                                  % "3.2.8"       % Test,
    "com.typesafe.play"       %% "play-test"                                  % current       % Test,
    "org.pegdown"             %  "pegdown"                                    % "1.6.0"       % Test,
    "org.scalatestplus.play"  %% "scalatestplus-play"                         % "5.1.0"       % Test,
    "com.github.tomakehurst"  %  "wiremock-standalone"                        % "2.27.2"      % Test,
    "com.vladsch.flexmark"    %  "flexmark-all"                               % "0.36.8"      % Test,
    "uk.gov.hmrc"             %% "pay-api-cor-card-payment-journey-test-data" % payApiVersion % Test
  )

}