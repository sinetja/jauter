package jauter;

import java.util.Collections;

public class MethodlessRouter<T> {
  protected final NonorderedRouter<T> first = new NonorderedRouter<T>();
  protected final NonorderedRouter<T> other = new NonorderedRouter<T>();
  protected final NonorderedRouter<T> last  = new NonorderedRouter<T>();

  protected T notFound;

  //----------------------------------------------------------------------------

  public MethodlessRouter<T> pattern(String path, T target) {
    other.pattern(path, target);
    return this;
  }

  public MethodlessRouter<T> patternFirst(String path, T target) {
    first.pattern(path, target);
    return this;
  }

  public MethodlessRouter<T> patternLast(String path, T target) {
    last.pattern(path, target);
    return this;
  }

  public MethodlessRouter<T> notFound(T target) {
    this.notFound = target;
    return this;
  }

  //----------------------------------------------------------------------------

  public void removeTarget(T target) {
    first.removeTarget(target);
    other.removeTarget(target);
    last .removeTarget(target);
  }

  public void removePath(String path) {
    first.removePath(path);
    other.removePath(path);
    last .removePath(path);
  }

  //----------------------------------------------------------------------------

  public Routed<T> route(String path) {
    Routed<T> ret = first.route(path);
    if (ret != null) return ret;

    ret = other.route(path);
    if (ret != null) return ret;

    ret = last.route(path);
    if (ret != null) return ret;

    if (notFound != null) return new Routed<T>(notFound, true, Collections.<String, String>emptyMap());

    return null;
  }

  public String path(T target, Object... params) {
    String ret = first.path(target, params);
    if (ret != null) return ret;

    ret = other.path(target, params);
    if (ret != null) return ret;

    return last.path(target, params);
  }
}
