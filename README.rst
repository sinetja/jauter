This tiny Java library is intended for use together with an HTTP server side
library like Netty, to route a path to a target.

If you want to use with Netty, see `netty-router <https://github.com/xitrum-framework/netty-router>`_.

Use with Maven
~~~~~~~~~~~~~~

::

  <dependency>
    <groupId>tv.cntt</groupId>
    <artifactId>jauter</artifactId>
    <version>1.0</version>
  </dependency>

Create router
~~~~~~~~~~~~~

::

  import jauter.Router;

  // Create a router that routes paths to action classes.
  // This is just an example, any other target type is OK.
  Router router = new Router<Class<? extends MyAction>>();

Add routing rules
~~~~~~~~~~~~~~~~~

::

  router.pattern("/articles",             MyArticleIndex.class);
  router.pattern("/articles/:id",         MyArticleShow.class);
  router.pattern("/articles/:id.:format", MyArticleShow.class);

The router only cares about the path, not HTTP method.
You should create a router for each HTTP method.

Match route
~~~~~~~~~~~

::

  import jauter.Routed;

  // routed.target will be MyArticleShow.class.
  // routed.params will be a map of "id" -> "123".
  Routed routed = router.route("/articles/123");

  // routed.target will be MyArticleShow.class.
  // routed.params will be a map of "id" -> "123", "format" -> "json".
  Routed routed = router.route("/articles/123.json");

You should pass only the path part of the request URL to ``route``.
Do not pass ``/articles/123?foo=bar`` or ``http://example.com/articles/123`` etc.

Create reverse route
~~~~~~~~~~~~~~~~~~~~

Without params:

::

  router.path(MyArticleIndex.class)  // => "/articles"

With params:

::

  Map<String, Object> params = new HashMap<String, Object>();
  params.put("id", 123);
  router.path(MyArticleShow.class, params)  // => "/articles/123"

With params (more convenient):

::

  router.path(MyArticleShow.class, "id", 123)                    // => "/articles/123"
  router.path(MyArticleShow.class, "id", 123, "format", "json")  // => "/articles/123.json"

Additional params will be put to the query part:

::

  router.path(MyArticleIndex.class, "x", 1, "y", 2)          // => "/articles?x=1&y=2"
  router.path(MyArticleShow.class, "id", 123, "foo", "bar")  // => "/articles/123?foo=bar"
