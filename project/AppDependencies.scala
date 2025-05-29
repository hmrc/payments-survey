import sbt.*

object AppDependencies {
  private val payApiVersion = "1.260.0-SNAPSHOT"
  private val bootstrapVersion = "9.12.0"

  val compile: Seq[ModuleID] = Seq(
    "uk.gov.hmrc"             %% "bootstrap-frontend-play-29"       % bootstrapVersion,
    "uk.gov.hmrc"             %% "play-frontend-hmrc-play-29"       % "12.1.0",
    "uk.gov.hmrc"             %% "pay-api-cor-card-payment-journey" % payApiVersion,
    "com.beachape"            %% "enumeratum"                       % "1.7.3"
  )

  val test: Seq[ModuleID] = Seq(
    "org.scalatest"           %% "scalatest"                                  % "3.2.19"             % Test,
    "org.scalatestplus.play"  %% "scalatestplus-play"                         % "6.0.1"              % Test,
    "com.github.tomakehurst" %   "wiremock"                                   % "3.0.0-beta-7"       % Test,
    "uk.gov.hmrc"             %% "pay-api-cor-card-payment-journey-test-data" % payApiVersion        % Test,
    "uk.gov.hmrc"             %% "bootstrap-test-play-29"                     % bootstrapVersion     % Test,
    "uk.gov.hmrc.mongo"       %% "hmrc-mongo-test-play-29"                    % "2.6.0"              % Test
  )
}
