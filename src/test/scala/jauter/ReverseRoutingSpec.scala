package jauter

import scala.collection.JavaConverters._
import org.scalatest._

class ReverseRoutingSpec extends FlatSpec with Matchers {
  val router = new Router[String]
  router.pattern("/articles",     "index")
  router.pattern("/articles/:id", "show")
  router.pattern("/download/:*",  "download")

  "A reverse router" should "handle empty params" in {
    router.path("index") should be ("/articles")
  }

  "A reverse router" should "handle map params" in {
    router.path("show", Map("id" -> 123).asJava)                     should be ("/articles/123")
    router.path("show", Map("id" -> 123, "format" -> "json").asJava) should be ("/articles/123?format=json")

    Seq("/articles/123?format=json&x=1", "/articles/123?x=1&format=json")      should contain(
      router.path("show", Map("id" -> 123, "format" -> "json", "x" -> 1).asJava)
    )
  }

  "A reverse router" should "handle varargs" in {
    router.path("download", "*", "foo/bar.png") should be ("/download/foo/bar.png")
  }
}
