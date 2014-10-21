package jauter

import scala.collection.JavaConverters._
import org.scalatest._

class ReverseRoutingSpec extends FlatSpec with Matchers {
  val router = new Router[String]
  router.pattern("/articles",             "index")
  router.pattern("/articles/:id",         "show")
  router.pattern("/articles/:id/:format", "show")
  router.pattern("/download/:*",          "download")

  "A reverse router" should "handle empty params" in {
    router.path("index") should be ("/articles")
  }

  "A reverse router" should "handle map params" in {
    router.path("show", Map("id" -> 123).asJava) should be ("/articles/123")
  }

  "A reverse router" should "handle varargs" in {
    router.path("download", "*", "foo/bar.png") should be ("/download/foo/bar.png")
  }

  "A reverse router" should "return path with minimum number of params in the query" in {
    router.path("show", Map("id" -> 123, "format" -> "json").asJava) should be ("/articles/123/json")

    Seq("/articles/123/json?x=1&y=2", "/articles/123/json?y=2&x=1") should contain(
      router.path("show", Map("id" -> 123, "format" -> "json", "x" -> 1, "y" -> 2).asJava)
    )
  }

  "A reverse router" should "handle class" in {
    trait Action
    class Index extends Action
    class Show  extends Action

    val router = new Router[Object]
    val index  = new Index
    router.pattern("/articles",     index)
    router.pattern("/articles/:id", classOf[Show])

    router.path(index)          should be ("/articles")
    router.path(classOf[Index]) should be ("/articles")

    router.path(new Show)                   should be (null)
    router.path(classOf[Show], "id", "123") should be ("/articles/123")

    router.path(classOf[Action]) shouldNot be (null)
  }
}
