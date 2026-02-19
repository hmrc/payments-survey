import uk.gov.hmrc.versioning.SbtGitVersioning.autoImport.majorVersion
import uk.gov.hmrc.ShellPrompt
import wartremover.Wart

val appName = "payments-survey"
scalaVersion := "3.3.7"

lazy val microservice = Project(appName, file("."))
  .enablePlugins(play.sbt.PlayScala, SbtDistributablesPlugin)
  .settings(commonSettings *)
  .settings(
    libraryDependencies ++= AppDependencies.compile ++ AppDependencies.test,
  )
  .settings(
    PlayKeys.playDefaultPort := 9966,
    routesImport ++= Seq(
      "controllers.Assets.Asset",
      "paysurvey.journey._",
      "payapi.corcommon.model._",
      "langswitch.Language"
    ))
  .settings(
    commands += Command.command("runTestOnly") { state =>
      state.globalLogging.full.info("running play using 'testOnlyDoNotUseInAppConf' routes...")
      s"""set javaOptions += "-Dplay.http.router=testOnlyDoNotUseInAppConf.Routes"""" ::
        "run" ::
        s"""set javaOptions -= "-Dplay.http.router=testOnlyDoNotUseInAppConf.Routes"""" ::
        state
    }
  )
  .settings(
    //For some reason SBT was adding the stray string "utf8" to the compiler arguments and it was causing the 'doc' task to fail
    //This removes it again but it's not an ideal solution as I can't work out why this is being added in the first place.
    Compile / scalacOptions -= "utf8"
  )
  .settings(Compile / doc / scalacOptions := Seq() ) //this will allow to have warnings in `doc` task)
  .disablePlugins(JUnitXmlReportPlugin) //Required to prevent https://github.com/scalatest/scalatest/issues/1427

lazy val scalaCompilerOptions = Seq(
  "-Werror",
  "-Wunused:implicits",
  "-Wunused:imports",
  "-Wunused:locals",
  "-Wunused:params",
  "-Wunused:privates",
  "-Wvalue-discard",
  "-deprecation",
  "-feature",
  "-unchecked",
  "-language:implicitConversions",
  "-language:strictEquality",
  "-Wconf:msg=unused import&src=html/.*:s",
  "-Wconf:src=routes/.*:s"
)

lazy val wartRemoverSettings =
  Seq(
    (Compile / compile / wartremoverErrors) ++= Warts.allBut(
      Wart.DefaultArguments,
      Wart.ImplicitConversion,
      Wart.ImplicitParameter,
      Wart.Nothing,
      Wart.Null,
      Wart.Overloading,
      Wart.SizeIs,
      Wart.SortedMaxMinOption,
      Wart.Throw,
      Wart.ToString
    ),
    Test / compile / wartremoverErrors --= Seq(
      Wart.Any,
      Wart.Equals,
      Wart.GlobalExecutionContext,
      Wart.Null,
      Wart.NonUnitStatements,
      Wart.PublicInference,
      Wart.SeqApply
    ),
    wartremoverExcluded ++= (
      (baseDirectory.value ** "*.sc").get ++
        (Compile / routes).value
      ),
  )

lazy val scoverageSettings = {
  import scoverage.ScoverageKeys
  Seq(
    // Semicolon-separated list of regexs matching classes to exclude
    ScoverageKeys.coverageExcludedPackages := "<empty>;.*BuildInfo.*;Reverse.*;app.Routes.*;prod.*;testonly.*;testOnlyDoNotUseInProd.*;manualdihealth.*;forms.*;config.*;",
    ScoverageKeys.coverageExcludedFiles := "<empty>;.*microserviceGlobal.*;.*microserviceWiring.*;.*ApplicationLoader.*;.*ApplicationConfig.*;.*package.*;.*Routes.*;.*TestOnlyController.*;.*util.*;.*data_layer.*;",
    ScoverageKeys.coverageMinimumStmtTotal := 80,
    ScoverageKeys.coverageFailOnMinimum := true,
    ScoverageKeys.coverageHighlighting := true
  )
}

lazy val commonSettings = Seq(
  majorVersion := 0,
  //For some reason SBT was adding the stray string "-encoding" to the compiler arguments and it was causing the build to fail
  //This removes it again, but it's not an ideal solution as I can't work out why this is being added in the first place.
  Compile / scalacOptions -= "-encoding",
  scalacOptions ++= scalaCompilerOptions,
  wartremoverExcluded ++=
    (baseDirectory.value / "it").get ++
      (baseDirectory.value / "test").get ++
      Seq(sourceManaged.value / "main" / "sbt-buildinfo" / "BuildInfo.scala") ++
      (Compile / routes).value,
  shellPrompt := ShellPrompt(version.value),
  scalafmtOnCompile                := true,
)
  .++(wartRemoverSettings)
  .++(scoverageSettings)

