import ArrayLikeFields.Extract

object Main {
  case class Person(name:String)

  val res: Set[String] = ArrayLikeFields.extract[Person]

  def main(args: Array[String]): Unit = {
    println(res) //Set(name)

    import Lib._
    "abc".toOrgJson[Person] //Set(name)
  }
}

object Lib {
  trait JSONObject

  implicit class SomeImplicit(s: String) {
    def toOrgJson[T: Extract]: JSONObject = {
      val arrayLikeFields: Set[String] = ArrayLikeFields.extract[T]
      //some code, that uses fields, etc
      println(arrayLikeFields)
      null
    }
  }
}