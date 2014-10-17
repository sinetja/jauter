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

  "A router" should "route subclasses" in {
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
