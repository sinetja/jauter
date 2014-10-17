package jauter;

import java.util.Map;

public class Routed<T> {
  public final Map<String, String> params;
  public final T target;

  public Routed(Map<String, String> params, T target) {
    this.params = params;
    this.target = target;
  }
}
