package jauter;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * http://stackoverflow.com/questions/1069528/method-chaining-inheritance-don-t-play-well-together-java
 */
public abstract class Router<M, T, RouterLike extends Router<M, T, RouterLike>> {
  protected abstract RouterLike getThis();

  protected abstract M CONNECT();
  protected abstract M DELETE();
  protected abstract M GET();
  protected abstract M HEAD();
  protected abstract M OPTIONS();
  protected abstract M PATCH();
  protected abstract M POST();
  protected abstract M PUT();
  protected abstract M TRACE();

  //----------------------------------------------------------------------------

  protected final Map<M, MethodlessRouter<T>> routers =
      new HashMap<M, MethodlessRouter<T>>();

  protected final MethodlessRouter<T> anyMethodRouter =
      new MethodlessRouter<T>();

  protected T notFound;

  //----------------------------------------------------------------------------

  public RouterLike pattern(M method, String path, T target) {
    getMethodlessRouter(method).pattern(path, target);
    return getThis();
  }

  public RouterLike patternFirst(M method, String path, T target) {
    getMethodlessRouter(method).patternFirst(path, target);
    return getThis();
  }

  public RouterLike patternLast(M method, String path, T target) {
    getMethodlessRouter(method).patternLast(path, target);
    return getThis();
  }

  public RouterLike notFound(T target) {
    this.notFound = target;
    return getThis();
  }

  private MethodlessRouter<T> getMethodlessRouter(M method) {
    if (method == null) return anyMethodRouter;

    MethodlessRouter<T> r = routers.get(method);
    if (r == null) {
      r = new MethodlessRouter<T>();
      routers.put(method, r);
    }

    return r;
  }

  //----------------------------------------------------------------------------

  public void removeTarget(T target) {
    for (MethodlessRouter<T> r : routers.values()) r.removeTarget(target);
    anyMethodRouter.removeTarget(target);
  }

  public void removePath(String path) {
    for (MethodlessRouter<T> r : routers.values()) r.removePath(path);
    anyMethodRouter.removePath(path);
  }

  //----------------------------------------------------------------------------

  public Routed<T> route(M method, String path) {
    MethodlessRouter<T> router = routers.get(method);
    if (router == null) router = anyMethodRouter;

    Routed<T> ret = router.route(path);
    if (ret != null) return ret;

    if (router != anyMethodRouter) {
      ret = anyMethodRouter.route(path);
      if (ret != null) return ret;
    }

    if (notFound != null) return new Routed<T>(notFound, true, Collections.<String, String>emptyMap());

    return null;
  }

  //----------------------------------------------------------------------------
  // Reverse routing.

  public String path(M method, T target, Object... params) {
    MethodlessRouter<T> router = (method == null)? anyMethodRouter : routers.get(method);
    if (router == null) router = anyMethodRouter;

    String ret = router.path(target, params);
    if (ret != null) return ret;

    return (router == anyMethodRouter)? null : anyMethodRouter.path(target, params);
  }

  public String path(T target, Object... params) {
    Collection<MethodlessRouter<T>> rs = routers.values();
    for (MethodlessRouter<T> r : rs) {
      String ret = r.path(target, params);
      if (ret != null) return ret;
    }
    return anyMethodRouter.path(target, params);
  }

  //----------------------------------------------------------------------------

  public RouterLike CONNECT(String path, T target) {
    return pattern(CONNECT(), path, target);
  }

  public RouterLike DELETE(String path, T target) {
    return pattern(DELETE(), path, target);
  }

  public RouterLike GET(String path, T target) {
    return pattern(GET(), path, target);
  }

  public RouterLike HEAD(String path, T target) {
    return pattern(HEAD(), path, target);
  }

  public RouterLike OPTIONS(String path, T target) {
    return pattern(OPTIONS(), path, target);
  }

  public RouterLike PATCH(String path, T target) {
    return pattern(PATCH(), path, target);
  }

  public RouterLike POST(String path, T target) {
    return pattern(POST(), path, target);
  }

  public RouterLike PUT(String path, T target) {
    return pattern(PUT(), path, target);
  }

  public RouterLike TRACE(String path, T target) {
    return pattern(TRACE(), path, target);
  }

  public RouterLike ANY(String path, T target) {
    return pattern(null, path, target);
  }

  //----------------------------------------------------------------------------

  public RouterLike CONNECT_FIRST(String path, T target) {
    return patternFirst(CONNECT(), path, target);
  }

  public RouterLike DELETE_FIRST(String path, T target) {
    return patternFirst(DELETE(), path, target);
  }

  public RouterLike GET_FIRST(String path, T target) {
    return patternFirst(GET(), path, target);
  }

  public RouterLike HEAD_FIRST(String path, T target) {
    return patternFirst(HEAD(), path, target);
  }

  public RouterLike OPTIONS_FIRST(String path, T target) {
    return patternFirst(OPTIONS(), path, target);
  }

  public RouterLike PATCH_FIRST(String path, T target) {
    return patternFirst(PATCH(), path, target);
  }

  public RouterLike POST_FIRST(String path, T target) {
    return patternFirst(POST(), path, target);
  }

  public RouterLike PUT_FIRST(String path, T target) {
    return patternFirst(PUT(), path, target);
  }

  public RouterLike TRACE_FIRST(String path, T target) {
    return patternFirst(TRACE(), path, target);
  }

  public RouterLike ANY_FIRST(String path, T target) {
    return patternFirst(null, path, target);
  }

  //----------------------------------------------------------------------------

  public RouterLike CONNECT_LAST(String path, T target) {
    return patternLast(CONNECT(), path, target);
  }

  public RouterLike DELETE_LAST(String path, T target) {
    return patternLast(DELETE(), path, target);
  }

  public RouterLike GET_LAST(String path, T target) {
    return patternLast(GET(), path, target);
  }

  public RouterLike HEAD_LAST(String path, T target) {
    return patternLast(HEAD(), path, target);
  }

  public RouterLike OPTIONS_LAST(String path, T target) {
    return patternLast(OPTIONS(), path, target);
  }

  public RouterLike PATCH_LAST(String path, T target) {
    return patternLast(PATCH(), path, target);
  }

  public RouterLike POST_LAST(String path, T target) {
    return patternLast(POST(), path, target);
  }

  public RouterLike PUT_LAST(String path, T target) {
    return patternLast(PUT(), path, target);
  }

  public RouterLike TRACE_LAST(String path, T target) {
    return patternLast(TRACE(), path, target);
  }

  public RouterLike ANY_LAST(String path, T target) {
    return patternLast(null, path, target);
  }
}
