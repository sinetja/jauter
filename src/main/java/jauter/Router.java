package jauter;

public class Router<T> {
  protected final NonorderedRouter<T> first = new NonorderedRouter<T>();
  protected final NonorderedRouter<T> other = new NonorderedRouter<T>();
  protected final NonorderedRouter<T> last  = new NonorderedRouter<T>();

  //----------------------------------------------------------------------------

  public Router<T> pattern(String path, T target) {
    other.pattern(path, target);
    return this;
  }

  public Router<T> patternFirst(String path, T target) {
    first.pattern(path, target);
    return this;
  }

  public Router<T> patternLast(String path, T target) {
    last.pattern(path, target);
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

    return last.route(path);
  }

  public String path(T target, Object... params) {
    String ret = first.path(target, params);
    if (ret != null) return ret;

    ret = other.path(target, params);
    if (ret != null) return ret;

    return last.path(target, params);
  }
}
