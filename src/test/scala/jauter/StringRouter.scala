package jauter

object Method extends Enumeration {
  type Method = Value
  val CONNECT, DELETE, GET, HEAD, OPTIONS, PATCH, POST, PUT, TRACE = Value
}

class StringRouter extends Router[Method.Value, String, StringRouter] {
  protected def getThis: StringRouter = this

  protected def CONNECT = Method.CONNECT
  protected def DELETE  = Method.DELETE
  protected def GET     = Method.GET
  protected def HEAD    = Method.HEAD
  protected def OPTIONS = Method.OPTIONS
  protected def PATCH   = Method.PATCH
  protected def POST    = Method.POST
  protected def PUT     = Method.PUT
  protected def TRACE   = Method.TRACE
}

object StringMethodRouter {
  val router = (new StringRouter)
    .GET      ("/articles",             "index")
    .GET      ("/articles/:id",         "show")
    .GET      ("/articles/:id/:format", "show")
    .GET_FIRST("/articles/new",         "new")
    .POST     ("/articles",             "post")
    .PATCH    ("/articles/:id",         "patch")
    .DELETE   ("/articles/:id",         "delete")
    .ANY      ("/any",                  "any")
    .GET      ("/download/:*",          "download")
    .GET      ("/upload/:*",            "upload")
    .notFound ("404")
}
