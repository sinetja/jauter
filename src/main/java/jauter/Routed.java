package jauter;

import java.util.Map;

public class Routed<T> {
  private final T                   target;
  private final boolean             notFound;
  private final Map<String, String> params;

  public Routed(T target, boolean notFound, Map<String, String> params) {
    this.target   = target;
    this.notFound = notFound;
    this.params   = params;
  }

  public T target() {
    return target;
  }

  public boolean notFound() {
    return notFound;
  }

  public Map<String, String> params() {
    return params;
  }
}
