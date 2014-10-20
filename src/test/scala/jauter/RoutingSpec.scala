package jauter

import org.scalatest._

class RoutingSpec extends FlatSpec with Matchers {
  val router = new Router[String]
  router.pattern("/articles",     "index")
  router.pattern("/articles/:id", "show")
  router.pattern("/download/:*",  "download")

  "A router" should "handle empty params" in {
    val routed = router.route("/articles")
    routed.target      should be ("index")
    routed.params.size should be (0)
  }

  "A router" should "handle params" in {
    val routed = router.route("/articles/123")
    routed.target           should be ("show")
    routed.params.size      should be (1)
    routed.params.get("id") should be ("123")
  }

  "A router" should "handle none" in {
    val routed = router.route("/noexist")
    (routed == null) should be (true)
  }

  "A router" should "handle splat (wildcard)" in {
    val routed = router.route("/download/foo/bar.png")
    routed.target          should be ("download")
    routed.params.size     should be (1)
    routed.params.get("*") should be ("foo/bar.png")
  }

  "A router" should "handle subclasses" in {
    trait Action
    class Index extends Action
    class Show  extends Action

    val router = new Router[Class[_ <: Action]]
    router.pattern("/articles",     classOf[Index])
    router.pattern("/articles/:id", classOf[Show])

    val routed1 = router.route("/articles")
    val routed2 = router.route("/articles/123")
    routed1.target should be (classOf[Index])
    routed2.target should be (classOf[Show])
  }
}
