This tiny Java library is intended for use together with an HTTP server side
library (like Netty), to route a path to a target.

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
  Router router = new Router<Class[MyAction]>();  // Any target type will do

Add rules
~~~~~~~~~

::

  router.pattern("/articles",     MyArticleIndex.class);
  router.pattern("/articles/:id", MyArticleShow.class);

The router only cares about the path, not HTTP method.
You should create a router for each HTTP method.

Match route
~~~~~~~~~~~

::

  import jauter.Routed;

  // routed.params will be a map of "id" -> "123".
  // routed.target will be MyArticleShow.class.
  Routed routed = router.route("/articles/123");

You should pass only the path part of the request URL to ``route``.
Do not pass ``/articles/123?foo=bar`` or ``http://example.com/articles/123`` etc. to it.

Create reverse route
~~~~~~~~~~~~~~~~~~~~

TODO
