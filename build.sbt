import com.typesafe.sbt.SbtScalariform.ScalariformKeys
import scalariform.formatter.preferences.*
import uk.gov.hmrc.versioning.SbtGitVersioning.autoImport.majorVersion
import uk.gov.hmrc.ShellPrompt
import wartremover.Wart

val appName = "payments-survey"
val scalaV = "2.13.16"
scalaVersion := scalaV

lazy val microservice = Project(appName, file("."))
  .enablePlugins(play.sbt.PlayScala, SbtDistributablesPlugin)
  .settings(commonSettings: _*)
  .settings(
    libraryDependencies ++= AppDependencies.compile ++ AppDependencies.test
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
  "-Xfatal-warnings",
  "-Ywarn-unused:-imports,-patvars,-privates,-locals,-explicits,-implicits,_",
  "-Xlint:-missing-interpolator,_",
  "-Ywarn-value-discard",
  "-Ywarn-dead-code",
  "-deprecation",
  "-feature",
  "-unchecked",
  "-language:implicitConversions"
)

lazy val scalariformSettings: Def.SettingsDefinition = {
  // description of options found here -> https://github.com/scala-ide/scalariform
  ScalariformKeys.preferences := ScalariformKeys.preferences.value
    .setPreference(AlignArguments, true)
    .setPreference(AlignParameters, true)
    .setPreference(AlignSingleLineCaseStatements, true)
    .setPreference(AllowParamGroupsOnNewlines, true)
    .setPreference(CompactControlReadability, false)
    .setPreference(CompactStringConcatenation, false)
    .setPreference(DanglingCloseParenthesis, Force)
    .setPreference(DoubleIndentConstructorArguments, true)
    .setPreference(DoubleIndentMethodDeclaration, true)
    .setPreference(FirstArgumentOnNewline, Force)
    .setPreference(FirstParameterOnNewline, Force)
    .setPreference(FormatXml, true)
    .setPreference(IndentLocalDefs, true)
    .setPreference(IndentPackageBlocks, true)
    .setPreference(IndentSpaces, 2)
    .setPreference(IndentWithTabs, false)
    .setPreference(MultilineScaladocCommentsStartOnFirstLine, false)
    .setPreference(NewlineAtEndOfFile, true)
    .setPreference(PlaceScaladocAsterisksBeneathSecondAsterisk, false)
    .setPreference(PreserveSpaceBeforeArguments, true)
    .setPreference(RewriteArrowSymbols, false)
    .setPreference(SpaceBeforeColon, false)
    .setPreference(SpaceBeforeContextColon, false)
    .setPreference(SpaceInsideBrackets, false)
    .setPreference(SpaceInsideParentheses, false)
    .setPreference(SpacesAroundMultiImports, false)
    .setPreference(SpacesWithinPatternBinders, true)
}

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
    Compile / doc / wartremoverErrors := Seq()
  )

lazy val scoverageSettings = {
  import scoverage.ScoverageKeys
  Seq(
    // Semicolon-separated list of regexs matching classes to exclude
    ScoverageKeys.coverageExcludedPackages := "<empty>;.*BuildInfo.*;Reverse.*;app.Routes.*;prod.*;testOnlyDoNotUseInProd.*;manualdihealth.*;forms.*;config.*;",
    ScoverageKeys.coverageExcludedFiles := ".*microserviceGlobal.*;.*microserviceWiring.*;.*ApplicationLoader.*;.*ApplicationConfig.*;.*package.*;.*Routes.*;.*TestOnlyController.*;.*WebService.*;.*views.*",
    ScoverageKeys.coverageMinimumStmtTotal := 80,
    ScoverageKeys.coverageFailOnMinimum := true,
    ScoverageKeys.coverageHighlighting := true
  )
}

lazy val commonSettings = Seq(
  scalaVersion := scalaV,
  majorVersion := 0,
  //For some reason SBT was adding the stray string "-encoding" to the compiler arguments and it was causing the build to fail
  //This removes it again but it's not an ideal solution as I can't work out why this is being added in the first place.
  Compile / scalacOptions -= "-encoding",
  scalacOptions ++= scalaCompilerOptions,
  wartremoverExcluded ++=
    (baseDirectory.value / "it").get ++
      (baseDirectory.value / "test").get ++
      Seq(sourceManaged.value / "main" / "sbt-buildinfo" / "BuildInfo.scala") ++
      (Compile / routes).value,
  scalariformSettings,
  shellPrompt := ShellPrompt(version.value),
  libraryDependencySchemes += "org.scala-lang.modules" %% "scala-xml" % VersionScheme.Always
)
  .++(wartRemoverSettings)
  .++(Seq(
    Test / compile / wartremoverErrors --= Seq(
      Wart.Any,
      Wart.Equals,
      Wart.Null,
      Wart.NonUnitStatements,
      Wart.PublicInference
    )
  ))
  .++(scoverageSettings)

