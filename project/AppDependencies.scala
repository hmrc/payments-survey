import sbt.*

object AppDependencies {
  private val payApiVersion = "1.285.0"
  private val bootstrapVersion = "10.4.0"

  val compile: Seq[ModuleID] = Seq(
    "uk.gov.hmrc"             %% "bootstrap-frontend-play-30"       % bootstrapVersion,
    "uk.gov.hmrc"             %% "play-frontend-hmrc-play-30"       % "12.21.0",
    "uk.gov.hmrc"             %% "pay-api-cor-card-payment-journey" % payApiVersion,
    "com.beachape"            %% "enumeratum"                       % "1.9.2"
  )

  val test: Seq[ModuleID] = Seq(
    "org.scalatest"           %% "scalatest"                                  % "3.2.19",
    "org.scalatestplus.play"  %% "scalatestplus-play"                         % "7.0.2",
    "uk.gov.hmrc"             %% "pay-api-cor-card-payment-journey-test-data" % payApiVersion,
    "uk.gov.hmrc"             %% "bootstrap-test-play-30"                     % bootstrapVersion,
    "uk.gov.hmrc.mongo"       %% "hmrc-mongo-test-play-30"                    % "2.10.0"
  ).map(_ % Test)
}
