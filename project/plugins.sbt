resolvers += "hmrc-releases" at "https://artefacts.tax.service.gov.uk/artifactory/hmrc-releases/"
resolvers += "Typesafe Releases" at "https://repo.typesafe.com/typesafe/releases/"
resolvers += MavenRepository("HMRC-open-artefacts-maven2", "https://open.artefacts.tax.service.gov.uk/maven2")
resolvers += Resolver.url("HMRC-open-artefacts-ivy2", url("https://open.artefacts.tax.service.gov.uk/ivy2"))(Resolver.ivyStylePatterns)
resolvers += Resolver.mavenLocal

ThisBuild / libraryDependencySchemes += "org.scala-lang.modules" %% "scala-xml" % VersionScheme.Always

addSbtPlugin("org.playframework"  %% "sbt-plugin"            % "3.0.2")

addSbtPlugin("io.github.irundaia" %  "sbt-sassify"           % "1.5.2")

addSbtPlugin("org.scalariform"    %% "sbt-scalariform"       % "1.8.3")

addSbtPlugin("org.scoverage"      %  "sbt-scoverage"         % "2.0.9")

addSbtPlugin("org.scalastyle"     %% "scalastyle-sbt-plugin" % "1.0.0")

addSbtPlugin("org.wartremover"    %  "sbt-wartremover"       % "3.1.3")

addSbtPlugin("uk.gov.hmrc"        %  "sbt-auto-build"        % "3.12.0")

addSbtPlugin("uk.gov.hmrc"        %  "sbt-distributables"    % "2.5.0")
