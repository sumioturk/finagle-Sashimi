import AssemblyKeys._ // put this at the top of the file

assemblySettings

resolvers += "twitter-repo" at "http://maven.twttr.com"

resolvers += "eaio.com" at "http://eaio.com/maven2"

resolvers += "snatype" at "http://oss.sonatype.org/content/repositories/releases"


libraryDependencies ++= Seq(
  "com.twitter" % "finagle-core" % "5.3.9",
  "com.twitter" % "finagle-http" % "5.3.9",
  "com.twitter" % "finagle-redis" % "5.3.9",
  "com.twitter" % "util-eval" % "5.3.9",
  "org.slf4j" % "slf4j-api" % "1.7.2",
  //"org.slf4j" % "slf4j-simple" % "1.7.2",
  "ch.qos.logback" % "logback-core" % "1.0.7",
  "ch.qos.logback" % "logback-classic" % "1.0.7",
  "com.eaio.uuid" % "uuid" % "3.3",
  "org.scribe" % "scribe" % "1.3.0",
  "net.liftweb" % "lift-json_2.9.1" % "2.4",
  "org.specs2" %% "specs2" % "1.9" % "test",
   "org.mockito" % "mockito-all" % "1.9.0"
)

