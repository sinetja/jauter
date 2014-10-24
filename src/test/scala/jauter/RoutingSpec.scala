package jauter

import org.scalatest._

class RoutingSpec extends FlatSpec with Matchers {
  import StringMethodRouter.router

  "A router" should "ignore slashes at both ends" in {
    router.route(Method.GET, "articles").target should be ("index")
    router.route(Method.GET, "/articles").target should be ("index")
    router.route(Method.GET, "//articles").target should be ("index")
    router.route(Method.GET, "articles/").target should be ("index")
    router.route(Method.GET, "articles//").target should be ("index")
    router.route(Method.GET, "/articles/").target should be ("index")
    router.route(Method.GET, "//articles//").target should be ("index")
  }

  "A router" should "handle empty params" in {
    val routed = router.route(Method.GET, "/articles")
    routed.target      should be ("index")
    routed.params.size should be (0)
  }

  "A router" should "handle params" in {
    val routed = router.route(Method.GET, "/articles/123")
    routed.target           should be ("show")
    routed.params.size      should be (1)
    routed.params.get("id") should be ("123")
  }

  "A router" should "handle none" in {
    val router = (new MethodlessRouter[String]).pattern("/articles", "index")
    val routed = router.route("/noexist")
    (routed == null) should be (true)
  }

  "A router" should "handle splat (wildcard)" in {
    val routed = router.route(Method.GET, "/download/foo/bar.png")
    routed.target          should be ("download")
    routed.params.size     should be (1)
    routed.params.get("*") should be ("foo/bar.png")
  }

  "A router" should "handle subclasses" in {
    trait Action
    class Index extends Action
    class Show  extends Action

    val router = new MethodlessRouter[Class[_ <: Action]]
    router.pattern("/articles",     classOf[Index])
    router.pattern("/articles/:id", classOf[Show])

    val routed1 = router.route("/articles")
    val routed2 = router.route("/articles/123")
    routed1.target should be (classOf[Index])
    routed2.target should be (classOf[Show])
  }

  "A router" should "handle order" in {
    val routed1 = router.route(Method.GET, "/articles/new")
    routed1.target      should be ("new")
    routed1.params.size should be (0)

    val routed2 = router.route(Method.GET, "/articles/123")
    routed2.target           should be ("show")
    routed2.params.size      should be (1)
    routed2.params.get("id") should be ("123")

    val routed3 = router.route(Method.GET, "/notfound")
    routed3.target            should be ("404")
    routed3.params.size       should be (0)
  }

  "A router" should "handle any method" in {
    val routed1 = router.route(Method.GET, "/any")
    routed1.target      should be ("any")
    routed1.params.size should be (0)

    val routed2 = router.route(Method.POST, "/any")
    routed2.target      should be ("any")
    routed2.params.size should be (0)
  }

  "A router" should "handle remove by target" in {
    val router = (new MethodlessRouter[String]).pattern("/articles", "index")
    router.removeTarget("index")
    val routed = router.route("/articles")
    (routed == null) should be (true)
  }

  "A router" should "handle remove by path" in {
    val router = (new MethodlessRouter[String]).pattern("/articles", "index")
    router.removePath("/articles")
    val routed = router.route("/articles")
    (routed == null) should be (true)
  }
}
