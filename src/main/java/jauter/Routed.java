package jauter;

import java.util.Map;

public class Routed<T> {
  public final Map<String, String> params;
  public final T target;

  public Routed(T target, Map<String, String> params) {
    this.target = target;
    this.params = params;
  }
}
