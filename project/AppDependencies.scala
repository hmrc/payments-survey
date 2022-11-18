import play.core.PlayVersion.current
import sbt._

object AppDependencies {
  //todo remove pay-api stuff when we migrate
  private val payApiVersion = "1.83.0"

  val compile = Seq(
    "uk.gov.hmrc"             %% "bootstrap-frontend-play-28" % "5.4.0",
    "uk.gov.hmrc"             %% "play-frontend-govuk"              % "0.74.0-play-28",
    "uk.gov.hmrc"             %% "play-frontend-hmrc"               % "0.69.0-play-28",
    "uk.gov.hmrc.mongo"       %% "hmrc-mongo-play-28"               % "0.74.0",
    "uk.gov.hmrc.mongo"       %% "hmrc-mongo-test-play-28"          % "0.74.0",
    "uk.gov.hmrc"             %% "pay-api-cor-card-payment-journey" % payApiVersion,
    "com.beachape"            %% "enumeratum"                       % "1.6.1"
  )

  val test = Seq(
    "org.scalatest"           %% "scalatest"                                  % "3.2.8"       % Test,
    "uk.gov.hmrc"             %% "reactivemongo-test"                         % "5.0.0-play-28" % Test,
    "com.typesafe.play"       %% "play-test"                                  % current       % Test,
    "org.pegdown"             %  "pegdown"                                    % "1.6.0"       % Test,
    "org.scalatestplus.play"  %% "scalatestplus-play"                         % "5.1.0"       % Test,
    "com.github.tomakehurst"  %  "wiremock-standalone"                        % "2.27.2"      % Test,
    "com.vladsch.flexmark"    %  "flexmark-all"                               % "0.36.8"      % Test,
    "uk.gov.hmrc"             %% "pay-api-cor-card-payment-journey-test-data" % payApiVersion % Test
  )

}