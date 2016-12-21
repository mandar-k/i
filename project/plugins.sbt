//addSbtPlugin("com.lightbend.lagom" % "lagom-sbt-plugin" % "1.0.0")
//addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "3.0.0")
//addSbtPlugin("com.github.ddispaltro" % "sbt-reactjs" % "0.5.2")
//addSbtPlugin("com.lightbend.conductr" % "sbt-conductr" % "2.1.7")

// repository for Typesafe plugins
resolvers += "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases/"

addSbtPlugin("org.scala-js" % "sbt-scalajs" % "0.6.13")

addSbtPlugin("com.typesafe.sbt" % "sbt-less" % "1.0.6")

addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.0.0")

addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.5.9")

addSbtPlugin("com.vmunier" % "sbt-web-scalajs" % "1.0.3")

addSbtPlugin("com.typesafe.sbt" % "sbt-digest" % "1.1.0")

addSbtPlugin("com.typesafe.sbt" % "sbt-gzip" % "1.0.0")

addSbtPlugin("com.lightbend.lagom" % "lagom-sbt-plugin" % "1.3.0-M1")

//addSbtPlugin("com.lightbend.lagom" % "lagom-sbt-plugin" % "1.3.0-M1" excludeAll(ExclusionRule("io.netty", "netty-transport-native-epoll"), ExclusionRule("org.slf4j", "slf4j-simple")))
//addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.5.9" excludeAll(ExclusionRule("io.netty", "netty-transport-native-epoll"), ExclusionRule("org.slf4j", "slf4j-simple")))