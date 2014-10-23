package jauter;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

// http://stackoverflow.com/questions/1069528/method-chaining-inheritance-don-t-play-well-together-java

public abstract class Router<M, T> {
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

  //----------------------------------------------------------------------------

  public Router<M, T> pattern(M method, String path, T target) {
    getMethodlessRouter(method).pattern(path, target);
    return this;
  }

  public Router<M, T> patternFirst(M method, String path, T target) {
    getMethodlessRouter(method).patternFirst(path, target);
    return this;
  }

  public Router<M, T> patternLast(M method, String path, T target) {
    getMethodlessRouter(method).patternLast(path, target);
    return this;
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
  // Reverse routing.

  public String path(M method, T target, Object... params) {
    MethodlessRouter<T> router = (method == null)? anyMethodRouter : routers.get(method);
    return (router == null)? null : router.path(target);
  }

  public String path(T target, Object... params) {
    Collection<MethodlessRouter<T>> rs = routers.values();
    rs.add(anyMethodRouter);
    for (MethodlessRouter<T> r : rs) {
      String ret = r.path(target);
      if (r != null) return ret;
    }
    return null;
  }

  //----------------------------------------------------------------------------

  public Router<M, T> CONNECT(String path, T target) {
    return pattern(CONNECT(), path, target);
  }

  public Router<M, T> DELETE(String path, T target) {
    return pattern(DELETE(), path, target);
  }

  public Router<M, T> GET(String path, T target) {
    return pattern(GET(), path, target);
  }

  public Router<M, T> HEAD(String path, T target) {
    return pattern(HEAD(), path, target);
  }

  public Router<M, T> OPTIONS(String path, T target) {
    return pattern(OPTIONS(), path, target);
  }

  public Router<M, T> PATCH(String path, T target) {
    return pattern(PATCH(), path, target);
  }

  public Router<M, T> POST(String path, T target) {
    return pattern(POST(), path, target);
  }

  public Router<M, T> PUT(String path, T target) {
    return pattern(PUT(), path, target);
  }

  public Router<M, T> TRACE(String path, T target) {
    return pattern(TRACE(), path, target);
  }

  public Router<M, T> ANY(String path, T target) {
    return pattern(null, path, target);
  }

  //----------------------------------------------------------------------------

  public Router<M, T> CONNECT_FIRST(String path, T target) {
    return patternFirst(CONNECT(), path, target);
  }

  public Router<M, T> DELETE_FIRST(String path, T target) {
    return patternFirst(DELETE(), path, target);
  }

  public Router<M, T> GET_FIRST(String path, T target) {
    return patternFirst(GET(), path, target);
  }

  public Router<M, T> HEAD_FIRST(String path, T target) {
    return patternFirst(HEAD(), path, target);
  }

  public Router<M, T> OPTIONS_FIRST(String path, T target) {
    return patternFirst(OPTIONS(), path, target);
  }

  public Router<M, T> PATCH_FIRST(String path, T target) {
    return patternFirst(PATCH(), path, target);
  }

  public Router<M, T> POST_FIRST(String path, T target) {
    return patternFirst(POST(), path, target);
  }

  public Router<M, T> PUT_FIRST(String path, T target) {
    return patternFirst(PUT(), path, target);
  }

  public Router<M, T> TRACE_FIRST(String path, T target) {
    return patternFirst(TRACE(), path, target);
  }

  public Router<M, T> ANY_FIRST(String path, T target) {
    return patternFirst(null, path, target);
  }

  //----------------------------------------------------------------------------

  public Router<M, T> CONNECT_LAST(String path, T target) {
    return patternLast(CONNECT(), path, target);
  }

  public Router<M, T> DELETE_LAST(String path, T target) {
    return patternLast(DELETE(), path, target);
  }

  public Router<M, T> GET_LAST(String path, T target) {
    return patternLast(GET(), path, target);
  }

  public Router<M, T> HEAD_LAST(String path, T target) {
    return patternLast(HEAD(), path, target);
  }

  public Router<M, T> OPTIONS_LAST(String path, T target) {
    return patternLast(OPTIONS(), path, target);
  }

  public Router<M, T> PATCH_LAST(String path, T target) {
    return patternLast(PATCH(), path, target);
  }

  public Router<M, T> POST_LAST(String path, T target) {
    return patternLast(POST(), path, target);
  }

  public Router<M, T> PUT_LAST(String path, T target) {
    return patternLast(PUT(), path, target);
  }

  public Router<M, T> TRACE_LAST(String path, T target) {
    return patternLast(TRACE(), path, target);
  }

  public Router<M, T> ANY_LAST(String path, T target) {
    return patternLast(null, path, target);
  }
}
