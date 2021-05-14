resolvers += Resolver.url("HMRC Sbt Plugin Releases", url("https://dl.bintray.com/hmrc/sbt-plugin-releases"))(Resolver.ivyStylePatterns)
resolvers += "HMRC Releases" at "https://dl.bintray.com/hmrc/releases"
resolvers += "hmrc-releases" at "https://artefacts.tax.service.gov.uk/artifactory/hmrc-releases/"
resolvers += "Typesafe Releases" at "https://repo.typesafe.com/typesafe/releases/"
resolvers += "sonatype-releases" at "https://oss.sonatype.org/content/repositories/releases/"
resolvers += Resolver.typesafeRepo("releases")

addSbtPlugin("com.typesafe.play" %% "sbt-plugin" % "2.8.7")
addSbtPlugin("org.scalariform" % "sbt-scalariform" % "1.8.3")
addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.6.1")
addSbtPlugin("org.wartremover" % "sbt-wartremover" % "2.4.13")
addSbtPlugin("uk.gov.hmrc" % "sbt-artifactory" % "1.16.0")
addSbtPlugin("uk.gov.hmrc" % "sbt-auto-build" % "3.0.0")
addSbtPlugin("uk.gov.hmrc" % "sbt-bobby" % "3.4.0")
addSbtPlugin("uk.gov.hmrc" % "sbt-distributables" % "2.1.0")
addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "1.0.0")
addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.9.2")
addSbtPlugin("com.timushev.sbt" % "sbt-updates" % "0.3.4")
addSbtPlugin("org.irundaia.sbt"  % "sbt-sassify" % "1.5.1")