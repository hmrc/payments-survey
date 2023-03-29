import play.core.PlayVersion.current
import sbt._

object AppDependencies {
  private val payApiVersion = "1.117.0"

  val compile = Seq(
    "uk.gov.hmrc"             %% "bootstrap-frontend-play-28"       % "5.4.0",
    "uk.gov.hmrc"             %% "play-frontend-hmrc"               % "7.2.0-play-28",
    "uk.gov.hmrc.mongo"       %% "hmrc-mongo-play-28"               % "0.74.0",
    "uk.gov.hmrc"             %% "pay-api-cor-card-payment-journey" % payApiVersion,
    "com.beachape"            %% "enumeratum"                       % "1.6.1"
  )

  val test = Seq(
    "org.scalatest"           %% "scalatest"                                  % "3.2.8"       % Test,
    "com.typesafe.play"       %% "play-test"                                  % current       % Test,
    "uk.gov.hmrc.mongo"       %% "hmrc-mongo-test-play-28"                    % "0.74.0"      % Test,
    "org.pegdown"             %  "pegdown"                                    % "1.6.0"       % Test,
    "org.scalatestplus.play"  %% "scalatestplus-play"                         % "5.1.0"       % Test,
    "com.github.tomakehurst"  %  "wiremock-standalone"                        % "2.27.2"      % Test,
    "com.vladsch.flexmark"    %  "flexmark-all"                               % "0.36.8"      % Test,
    "uk.gov.hmrc"             %% "pay-api-cor-card-payment-journey-test-data" % payApiVersion % Test
  )

}
