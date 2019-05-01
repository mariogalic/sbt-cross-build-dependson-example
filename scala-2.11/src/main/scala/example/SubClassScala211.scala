package example

class SubClassScala211 extends ParentClass {
  override def whoami = "SubClassScala211"
}

object Run extends App {
  println((new SubClassScala211).whoami)
}