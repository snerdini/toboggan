import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "Toboggan"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
      // Add your project dependencies here,
      "postgresql" % "postgresql" % "8.4-702.jdbc4",
      "com.amazonaws" % "aws-java-sdk" % "1.3.11"
    )

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA).settings(
      // Add your own project settings here      
    )

}
