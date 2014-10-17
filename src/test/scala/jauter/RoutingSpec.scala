package jauter

import org.scalatest._

class RoutingSpec extends FlatSpec with Matchers {
  "A router" should "route empty params" in {
    val router = new Router[String]
    router.pattern("/articles",     "index")
    router.pattern("/articles/:id", "show")

    val routed = router.route("/articles")
    routed.target      should be ("index")
    routed.params.size should be (0)
  }

  "A router" should "route params" in {
    val router = new Router[String]
    router.pattern("/articles",     "index")
    router.pattern("/articles/:id", "show")

    val routed = router.route("/articles/123")
    routed.target           should be ("show")
    routed.params.size      should be (1)
    routed.params.get("id") should be ("123")
  }

  "A router" should "route none" in {
    val router = new Router[String]
    router.pattern("/articles",     "index")
    router.pattern("/articles/:id", "show")

    val routed = router.route("/noexist")
    (routed == null) should be (true)
  }
}
