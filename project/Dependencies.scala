import sbt._

object Dependencies {
  case object com {
    case object olegpy {
      val `better-monadic-for` =
        "com.olegpy" %% "better-monadic-for" % "0.3.1"
    }
    case object beachape {
      val enumeratumCirceVersion = "1.6.1"
      val enumeratum = "com.beachape" %% "enumeratum" % enumeratumCirceVersion
      val `enumeratum-circe` = "com.beachape" %% "enumeratum-circe" % enumeratumCirceVersion
    }
    case object kubukoz {
      val `better-tostring` = "com.kubukoz" % "better-tostring" % "0.2.6" cross CrossVersion.full
    }
    case object monovore {
      val `decline-effect` = "com.monovore" %% "decline-effect" % "1.3.0"
      
    }
  }

  case object org {
    case object augustjune {
      val `context-applied` =
        "org.augustjune" %% "context-applied" % "0.1.4"
    }

    case object scalatest {
      val scalatest =
        "org.scalatest" %% "scalatest" % "3.2.3"
    }

    case object typelevel {

      val `kind-projector` =
        "org.typelevel" %% "kind-projector" % "0.11.3" cross CrossVersion.full

      val `cats-effect` =
        "org.typelevel" %% "cats-effect" % "2.3.1"
    }

    case object http4s {
      val http4sVersion = "0.21.15"
      val `http4s-dsl` = "org.http4s" %% "http4s-dsl" % http4sVersion
      val `http4s-blaze-client` = "org.http4s" %% "http4s-blaze-client" % http4sVersion
      val `http4s-circe` = "org.http4s" %% "http4s-circe" % http4sVersion
    }
  }

  case object circe {
    val `circe-generic` = "io.circe" %% "circe-generic" % "0.12.3"
    val `circe-generic-extras` = "io.circe" %% "circe-generic-extras" % "0.13.0"
  }

}
