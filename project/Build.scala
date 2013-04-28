import sbt._
import play.Project._

object ApplicationBuild extends Build {

    val appName         = "Toboggan"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
      jdbc,
      anorm,
      "postgresql" % "postgresql" % "8.4-702.jdbc4",
      "com.amazonaws" % "aws-java-sdk" % "1.3.11"
    )

//    val mainDeps = Seq()
//
//    lazy val admin = play.Project(
//      appName + "-admin", appVersion, appDependencies, path = file("modules/admin")
//    )

//    lazy val main = play.Project(appName, appVersion, mainDeps).settings(
//      // Add your own project settings here
//    ).dependsOn(admin).aggregate(admin)

      val main = play.Project(appName, appVersion, appDependencies).settings(
        // Add your own project settings here
      )
}
