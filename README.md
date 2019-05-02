# SBT cross-building multi-build project with dependsOn example

This examples shows how to [cross-build](https://www.scala-sbt.org/1.x/docs/Cross-Build.html) a project consisting of [multiple sub-projects](https://www.scala-sbt.org/1.x/docs/Multi-Project.html) where the sub-projects
of different Scala versions depend on a parent project. Also, it attempts to answer StackOverflow question 
[How to make different subclass for each scala version?](https://stackoverflow.com/questions/55887656/how-to-make-different-subclass-for-each-scala-version)  

Given `scala-parent` project, and `scala-2.11` and `scala-2.12` sub-projects that [`dependsOn`](https://github.com/sbt/sbt/blob/bc21db28646f0b4a4484cdd22b6a4c4596cafde1/main/src/main/scala/sbt/Project.scala#L249) `scala-parent`, then
set [`crossScalaVersions`](https://github.com/sbt/sbt/blob/bc21db28646f0b4a4484cdd22b6a4c4596cafde1/main/src/main/scala/sbt/Keys.scala#L202) on `scala-parent`, whilst setting [`scalaVersion`](https://github.com/sbt/sbt/blob/bc21db28646f0b4a4484cdd22b6a4c4596cafde1/main/src/main/scala/sbt/Keys.scala#L200) on sub-projects like so

```
lazy val scalaParent = (project in file("scala-parent")).settings(crossScalaVersions := Seq("2.11.0", "2.12.0"))
lazy val scala211  = (project in file("scala-2.11")).dependsOn(scalaParent).settings(scalaVersion := "2.11.0")
lazy val scala212  = (project in file("scala-2.12")).dependsOn(scalaParent).settings(scalaVersion := "2.12.0")
```

Also set [`crossScalaVersions := Seq()`](https://github.com/sbt/sbt/issues/4262#issuecomment-405607763) on the `root` project that [aggregates](https://www.scala-sbt.org/1.x/docs/Multi-Project.html#Aggregation) other projects:

```sbtshell
lazy val root = (project in file("."))
  .aggregate(scalaParent, scala211, scala212)
  .settings(
    name := "sbt-cross-build-dependson-example",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % Test,
    crossScalaVersions := Seq() // https://github.com/sbt/sbt/issues/4262#issuecomment-405607763
  )
```

Now executing `sbt +compile` should correctly cross-build the project:

```sbtshell
sbt:sbt-cross-build-dependson-example> +compile
[info] Setting Scala version to 2.12.0 on 2 projects.
...
[info] Updating scala212...
[info] Compiling 1 Scala source to /Users/mario/IdeaProjects/sbt-cross-build-dependson-example/scala-parent/target/scala-2.12/classes ...
[info] Compiling 1 Scala source to /Users/mario/IdeaProjects/sbt-cross-build-dependson-example/scala-2.12/target/scala-2.12/classes ...
...
[info] Setting Scala version to 2.11.0 on 2 projects.
...
[info] Updating scala211...
[info] Compiling 1 Scala source to /Users/mario/IdeaProjects/sbt-cross-build-dependson-example/scala-parent/target/scala-2.11/classes ...
[info] Compiling 1 Scala source to /Users/mario/IdeaProjects/sbt-cross-build-dependson-example/scala-2.11/target/scala-2.11/classes ...
```

Note how `scala-parent` has been cross-compiled for 2.11 and 2.12

```sbtshell
> tree -d -L 1 scala-parent/target/
scala-parent/target/
├── scala-2.11
├── scala-2.12
```

whilst sub-projects were build just for their respective versions

```sbtshell
> tree -d -L 1 scala-2.11/target/
scala-2.11/target/
├── scala-2.11

> tree -d -L 1 scala-2.12/target/
scala-2.12/target/
├── scala-2.12
``` 

