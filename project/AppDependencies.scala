import play.core.PlayVersion.current
import sbt._

object AppDependencies {

  private lazy val catsVersion = "1.1.0"

  val compile = Seq(
    "uk.gov.hmrc"             %% "govuk-template"                   % "5.36.0-play-26",
    "uk.gov.hmrc"             %% "play-ui"                          % "7.40.0-play-26",
    "uk.gov.hmrc"             %% "bootstrap-play-26"                % "0.42.0",
    "uk.gov.hmrc"             %% "pay-api-cor-card-payment-journey" % "[0.350.0,)",
    "com.beachape"            %% "enumeratum" % "1.5.13",
    "org.typelevel"           %% "cats-core" % catsVersion,
    "org.typelevel"           %% "cats-kernel" % catsVersion,
    "org.typelevel"           %% "cats-macros" % catsVersion
  )

  val test = Seq(
    "org.scalatest"           %% "scalatest"                % "3.0.4"                 % "test",
    "com.typesafe.play"       %% "play-test"                % current                 % "test",
    "org.pegdown"             %  "pegdown"                  % "1.6.0"                 % "test, it",
    "org.scalatestplus.play"  %% "scalatestplus-play"       % "2.0.0"                 % "test, it"
  )

}
