scalaVersion := "3.2.2"
libraryDependencies ++= Seq(
  "io.circe"      %% "circe-parser"   % "0.14.4",
  "io.circe"      %% "circe-generic"  % "0.14.4",
  "org.typelevel" %% "cats-effect"    % "3.4.7",
  "com.monovore"  %% "decline-effect" % "2.4.1"
)
assembly / assemblyJarName := "jmh-plot.jar"
