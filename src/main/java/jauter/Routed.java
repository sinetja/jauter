package jauter;

import java.util.Map;

public class Routed<T> {
  private final Map<String, String> params;
  private final T target;

  public Routed(T target, Map<String, String> params) {
    this.target = target;
    this.params = params;
  }

  public T target() {
    return target;
  }

  public Map<String, String> params() {
    return params;
  }
}
