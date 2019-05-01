package example

class SubClassScala212 extends ParentClass {
  override def whoami = "SubClassScala212"
}

object Run extends App {
  println((new SubClassScala212).whoami)
}