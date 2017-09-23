lazy val root = project.in(file("."))
            .settings(name := "gitvik",
                      description := "Recyclable",
                      version := "0.1",
                      scalaVersion := "2.12.3",
                      libraryDependencies += "com.chuusai" %% "shapeless" % "2.3.2"
                      )