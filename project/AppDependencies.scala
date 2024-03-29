import sbt.*

object AppDependencies {
  private val payApiVersion = "1.213.0"
  private val bootstrapPlay28Version = "7.20.0"

  val compile: Seq[ModuleID] = Seq(
    "uk.gov.hmrc"             %% "bootstrap-frontend-play-28"       % bootstrapPlay28Version,
    "uk.gov.hmrc"             %% "play-frontend-hmrc-play-28"       % "8.5.0",
    "uk.gov.hmrc"             %% "pay-api-cor-card-payment-journey" % payApiVersion,
    "com.beachape"            %% "enumeratum"                       % "1.7.3"
  )

  val test: Seq[ModuleID] = Seq(
    "org.scalatest"           %% "scalatest"                                  % "3.2.16"             % Test,
    "org.scalatestplus.play"  %% "scalatestplus-play"                         % "5.1.0"              % Test,
    "org.pegdown"             %  "pegdown"                                    % "1.6.0"              % Test,
    "com.github.tomakehurst"  %  "wiremock-standalone"                        % "2.27.2"             % Test,
    "com.vladsch.flexmark"    %  "flexmark-all"                               % "0.64.6"             % Test,
    "uk.gov.hmrc"             %% "pay-api-cor-card-payment-journey-test-data" % payApiVersion        % Test,
    "uk.gov.hmrc"             %% "bootstrap-test-play-28"                     % bootstrapPlay28Version % Test,
    "uk.gov.hmrc.mongo"       %% "hmrc-mongo-test-play-28"                    % "1.7.0"              % Test
  )
}
