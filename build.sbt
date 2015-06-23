name := "strips"

organization := "com.github.mrmechko"

version := "1.2.0-SNAPSHOT"

scalaVersion := "2.11.6"

resolvers += Resolver.sonatypeRepo("releases")

resolvers += Resolver.sonatypeRepo("snapshots")

libraryDependencies += "org.scalamock" %% "scalamock-scalatest-support" % "3.2" % "test"

libraryDependencies += "com.github.mrmechko" %% "swordnet" % "2.0-SNAPSHOT"

libraryDependencies += "com.typesafe.play" %% "play-json" % "2.4.0"

libraryDependencies := {
  CrossVersion.partialVersion(scalaVersion.value) match {
    // if scala 2.11+ is used, add dependency on scala-xml module
    case Some((2, scalaMajor)) if scalaMajor >= 11 =>
      libraryDependencies.value ++ Seq("org.scala-lang.modules" %% "scala-xml" % "1.0.3", "org.scalatest" % "scalatest_2.11" % "2.2.4" % "test")
    case Some((2, scalaMajor)) if scalaMajor == 10 =>
      libraryDependencies.value ++ Seq("org.scalatest" % "scalatest_2.10" % "2.2.4" % "test")
    case _ => libraryDependencies.value ++ Seq()// or nothing since I only care about xml
  }
}

crossScalaVersions := Seq("2.10.4", "2.11.6")

//Publishing to Sonatype

publishMavenStyle := true

credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")

publishTo <<= version { (v: String) =>
  val nexus = "https://oss.sonatype.org/"
  if (v.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

pomExtra := (
  <url>http://mrmechko.github.io/STrips</url>
    <licenses>
      <license>
        <name>BSD-style</name>
        <url>http://www.opensource.org/licenses/bsd-license.php</url>
        <distribution>repo</distribution>
      </license>
    </licenses>
    <scm>
      <url>git@github.com:mrmechko/strips.git</url>
      <connection>scm:git:git@github.com:mrmechko/strips.git</connection>
    </scm>
    <developers>
      <developer>
        <id>mrmechko</id>
        <name>Ritwik Bose</name>
        <url>http://cs.rochester.edu/~rbose</url>
      </developer>
    </developers>)
