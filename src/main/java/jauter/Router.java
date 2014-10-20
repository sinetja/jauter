package jauter;

import java.util.HashMap;
import java.util.Map;

public class Router<T> {
  private Map<String[], T> patterns = new HashMap<String[], T>();

  public void pattern(String path, T target) {
    String[] parts = path.split("/|\\.");
    patterns.put(parts, target);
  }

  public Routed<T> route(String path) {
    String[] parts = path.split("/|\\.");
    for (Map.Entry<String[], T> entry : patterns.entrySet()) {
      String[] currParts = entry.getKey();
      T        target    = entry.getValue();

      boolean             matched = true;
      Map<String, String> params  = new HashMap<String, String>();

      if (parts.length == currParts.length) {
        for (int i = 0; i < currParts.length; i++) {
          String token = currParts[i];
          if (token.length() > 0 && token.charAt(0) == ':') {
            params.put(token.substring(1), parts[i]);
          } else if (!token.equals(parts[i])) {
            matched = false;
            break;
          }
        }
      } else {
        matched = false;
      }

      if (matched) {
        return new Routed<T>(target, params);
      }
    }

    return null;
  }
}
