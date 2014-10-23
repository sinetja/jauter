This Java library can route paths to targets and create paths from targets and
params (reverse routing).

This library is tiny, without additional dependencies, and is intended for use
together with an HTTP server side library. If you want to use with
`Netty <http://netty.io/>`_, see `netty-router <https://github.com/sinetja/netty-router>`_.

Create router
~~~~~~~~~~~~~

::

  import jauter.Router;

  Router<MyMethod, MyTarget> router = new Router<MyMethod, MyTarget>() {
    // Tell Router about your method types
    protected MyMethod CONNECT() { return myConnectMethod; }
    protected MyMethod DELETE()  { return myDeleteMethod ; }
    protected MyMethod GET()     { return myGetMethod    ; }
    protected MyMethod HEAD()    { return myHeadMethod   ; }
    protected MyMethod OPTIONS() { return myOptionsMethod; }
    protected MyMethod PATCH()   { return myPatchMethod  ; }
    protected MyMethod POST()    { return myPostMethod   ; }
    protected MyMethod PUT()     { return myPutMethod    ; }
    protected MyMethod TRACE()   { return myTraceMethod  ; }
  };

Add routes
~~~~~~~~~~

::

  // Below uses "Class<? extends MyAction" as target type
  router
    .GET      ("/articles",     MyArticleIndex.class)
    .GET      ("/articles/:id", MyArticleShow.class)
    .GET      ("/download/:*",  MyDownload.class)      // ":*" must be the last token
    .GET_FIRST("/articles/new", MyArticleNew.class)    // This will be matched first
    .ANY_LAST ("/:*",           My404NotFound.class);  // This will be matched last, any method will match

The router only cares about the path, not HTTP method.
You should create a router for each HTTP method.

Jauter ignores slashes at both ends, so these are the same:

::

  router.GET("articles",   MyArticleIndex.class)
  router.GET("/articles",  MyArticleIndex.class)
  router.GET("/articles/", MyArticleIndex.class)

You can remove routes by target or by path:

::

  router.removeTarget(MyArticleIndex.class)
  router.removePath("/articles")

Match route
~~~~~~~~~~~

::

  import jauter.Routed;

  Routed routed1 = router.route(myGetMethod, "/articles/123");
  // routed1.target() => MyArticleShow.class
  // routed1.params() => Map "id" -> "123"

  Routed routed2 = router.route(myGetMethod, "/download/foo/bar.png");
  // routed2.target() => MyDownload.class
  // routed2.params() => Map of "*" -> "foo/bar.png"

  Routed routed3 = router.route("/noexist");
  // => null

You should pass only the path part of the request URL to ``route``.
Do not pass ``/articles/123?foo=bar`` or ``http://example.com/articles/123`` etc.

Create path (reverse routing)
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

::

  router.path(myGetMethod, MyArticleIndex.class);
  // => "/articles"

You can skip method if there's no confusion:

::

  router.path(MyArticleIndex.class)  // => "/articles"
  router.path(NoExist.class)         // => null

With params:

::

  // Things in params will be converted to String
  Map<Object, Object> params = new HashMap<Object, Object>();
  params.put("id", 123);
  router.path(MyArticleShow.class, params)  // => "/articles/123"

With params (more convenient):

::

  router.path(MyArticleShow.class, "id", 123)     // => "/articles/123"
  router.path(MyDownload.class,    "foo/bar.png") // => "/download/foo/bar.png"

Additional params will be put to the query part:

::

  router.path(MyArticleIndex.class, "x", 1, "y", 2)              // => "/articles?x=1&y=2"
  router.path(MyArticleShow.class, "id", 123, "format", "json")  // => "/articles/123?format=json"

You can specify an instance in pattern, but use the instance's class to create
path. This feature is useful if you want to create web frameworks:

::

  // Optimize speed by precreating.
  // Optimize memory by sharing for all requests.
  MyArticleIndex cachedInstance = new MyArticleIndex();

  Router router = new Router<Object>()
    .pattern("/articles",     cachedInstance)
    .pattern("/articles/:id", MyArticleShow.class);

  // These are the same:
  router.path(cachedInstance);
  router.path(MyArticleIndex.class);

Use with Maven
~~~~~~~~~~~~~~

::

  <dependency>
    <groupId>tv.cntt</groupId>
    <artifactId>jauter</artifactId>
    <version>1.3</version>
  </dependency>
