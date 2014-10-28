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

  /**
   * When target is a class, this method calls "newInstance" on the class.
   * Otherwise it returns the target as is.
   *
   * @return null if target is null
   */
  public static Object instanceFromTarget(Object target) throws InstantiationException, IllegalAccessException {
    if (target == null) return null;

    if (target instanceof Class) {
      // Create handler from class
      Class<?> klass = (Class<?>) target;
      return klass.newInstance();
    } else {
      return target;
    }
  }

  /**
   * When target is a class, this method calls "newInstance" on the class.
   * Otherwise it returns the target as is.
   *
   * @return null if target is null
   */
  public Object instanceFromTarget() throws InstantiationException, IllegalAccessException {
    return Routed.instanceFromTarget(target());
  }
}
