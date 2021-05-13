import com.typesafe.sbt.SbtScalariform.ScalariformKeys
import scalariform.formatter.preferences.{AlignArguments, AlignParameters, AlignSingleLineCaseStatements, AllowParamGroupsOnNewlines, CompactControlReadability, CompactStringConcatenation, DanglingCloseParenthesis, DoubleIndentConstructorArguments, DoubleIndentMethodDeclaration, FirstArgumentOnNewline, FirstParameterOnNewline, Force, FormatXml, IndentLocalDefs, IndentPackageBlocks, IndentSpaces, IndentWithTabs, MultilineScaladocCommentsStartOnFirstLine, NewlineAtEndOfFile, PlaceScaladocAsterisksBeneathSecondAsterisk, PreserveSpaceBeforeArguments, RewriteArrowSymbols, SpaceBeforeColon, SpaceBeforeContextColon, SpaceInsideBrackets, SpaceInsideParentheses, SpacesAroundMultiImports, SpacesWithinPatternBinders}
import uk.gov.hmrc.DefaultBuildSettings.{integrationTestSettings, scalaSettings}
import uk.gov.hmrc.{SbtArtifactory, ShellPrompt}
import uk.gov.hmrc.sbtdistributables.SbtDistributablesPlugin.publishingSettings
import wartremover.Wart

val appName = "payments-survey"
scalaVersion := "2.12.12"

lazy val microservice = Project(appName, file("."))
  .enablePlugins(
    play.sbt.PlayScala,
    SbtAutoBuildPlugin,
    SbtGitVersioning,
    SbtDistributablesPlugin,
    SbtArtifactory
  )
  .settings(
    libraryDependencies ++= AppDependencies.compile ++ AppDependencies.test
  )
  .settings(commonSettings: _*)
  .settings(
    PlayKeys.playDefaultPort := 9966,
    routesImport ++= Seq(
      "controllers.Assets.Asset",
      "payapi.corcommon.model._",
      "langswitch.Language"
    ))
  .settings(publishingSettings: _*)
  .settings(
    commands += Command.command("runTestOnly") { state =>
      state.globalLogging.full.info("running play using 'testOnlyDoNotUseInAppConf' routes...")
      s"""set javaOptions += "-Dplay.http.router=testOnlyDoNotUseInProd.Routes"""" ::
        "run" ::
        s"""set javaOptions -= "-Dplay.http.router=testOnlyDoNotUseInProd.Routes"""" ::
        state
    }
  )
  .settings(
    //For some reason SBT was adding the stray string "utf8" to the compiler arguments and it was causing the 'doc' task to fail
    //This removes it again but it's not an ideal solution as I can't work out why this is being added in the first place.
    scalacOptions in Compile -= "utf8")
  .disablePlugins(JUnitXmlReportPlugin) //Required to prevent https://github.com/scalatest/scalatest/issues/1427

lazy val scalaCompilerOptions = Seq(
  "-Xfatal-warnings",
  "-Ywarn-unused:-imports,-patvars,-privates,-locals,-explicits,-implicits,_",
  "-Xlint:-missing-interpolator,_",
  "-Yno-adapted-args",
  "-Ywarn-value-discard",
  "-Ywarn-dead-code",
  "-deprecation",
  "-feature",
  "-unchecked",
  "-language:implicitConversions",
  "-Ypartial-unification"
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

lazy val wartRemoverWarning = {
  val warningWarts = Seq(
    //Wart.Any
  )
  wartremoverWarnings in(Compile, compile) ++= warningWarts
}

lazy val wartRemoverError = {
  // Error
  val errorWarts: Seq[Wart] = Seq(
    Wart.JavaSerializable,
    Wart.AsInstanceOf,
    Wart.IsInstanceOf,
    Wart.ArrayEquals,
    Wart.AnyVal,
    Wart.EitherProjectionPartial,
    Wart.Enumeration,
    Wart.ExplicitImplicitTypes,
    Wart.FinalVal,
    Wart.JavaConversions,
    Wart.JavaSerializable,
    Wart.LeakingSealed,
    Wart.MutableDataStructures,
    Wart.Null,
    Wart.OptionPartial,
    Wart.Recursion,
    Wart.Return,
    Wart.TraversableOps,
    Wart.TryPartial,
    Wart.Var,
    Wart.While,
    Wart.FinalCaseClass,
    Wart.StringPlusAny
  )
  wartremoverErrors in(Compile, compile) ++= errorWarts
}

lazy val scoverageSettings = {
  import scoverage.ScoverageKeys
  Seq(
    // Semicolon-separated list of regexs matching classes to exclude
    ScoverageKeys.coverageExcludedPackages := "<empty>;.*BuildInfo.*;Reverse.*;app.Routes.*;prod.*;testOnlyDoNotUseInProd.*;manualdihealth.*;forms.*;config.*;",
    ScoverageKeys.coverageExcludedFiles := ".*microserviceGlobal.*;.*microserviceWiring.*;.*ApplicationLoader.*;.*ApplicationConfig.*;.*package.*;.*Routes.*;.*TestOnlyController.*;.*WebService.*",
    ScoverageKeys.coverageMinimum := 80,
    ScoverageKeys.coverageFailOnMinimum := false,
    ScoverageKeys.coverageHighlighting := true
  )
}

lazy val commonSettings = Seq(
  majorVersion := 0,
  scalacOptions ++= scalaCompilerOptions,
  resolvers ++= Seq(
    "hmrc-releases" at "https://artefacts.tax.service.gov.uk/artifactory/hmrc-releases/",
    "third-party-maven-releases" at "https://artefacts.tax.service.gov.uk/artifactory/third-party-maven-releases/",
    Resolver.bintrayRepo("hmrc", "releases"),
    Resolver.jcenterRepo,
    "third-party-maven-releases" at "https://artefacts.tax.service.gov.uk/artifactory/third-party-maven-releases/"
  ),
  evictionWarningOptions in update := EvictionWarningOptions.default.withWarnScalaVersionEviction(false),
  wartremoverExcluded ++=
    (baseDirectory.value / "it").get ++
      (baseDirectory.value / "test").get ++
      Seq(sourceManaged.value / "main" / "sbt-buildinfo" / "BuildInfo.scala") ++
      routes.in(Compile).value,
  scalariformSettings,
  shellPrompt := ShellPrompt(version.value)
)
  .++(wartRemoverError)
  .++(wartRemoverWarning)
  .++(Seq(
    wartremoverErrors in(Test, compile) --= Seq(Wart.Any, Wart.Equals, Wart.Null, Wart.NonUnitStatements, Wart.PublicInference)
  ))
  .++(scoverageSettings)
  .++(scalaSettings)
  .++(uk.gov.hmrc.DefaultBuildSettings.defaultSettings())

