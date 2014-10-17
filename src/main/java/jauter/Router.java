package jauter;

import java.util.HashMap;
import java.util.Map;

public class Router<T> {
  private Map<String, T> patterns = new HashMap<String, T>();

  public void pattern(String pathPattern, T target) {
    patterns.put(pathPattern, target);
  }

  public Routed<T> route(String path) {
    Routed<T> routed = null;

    String[] parts = path.split("/");
    for (String pattern : patterns.keySet()) {
      boolean matched = true;
      Map<String, String> params = new HashMap<String, String>();

      String[] currParts = pattern.split("/");

      if (parts.length == currParts.length) {
        for (int i = 0; i < currParts.length; i++) {
          if (currParts[i].startsWith(":")) {
            params.put(currParts[i].substring(1), parts[i]);
          } else if (!currParts[i].equals(parts[i])) {
            matched = false;
            break;
          }
        }
      } else {
        matched = false;
      }

      if (matched) {
        routed = new Routed<T>(patterns.get(pattern), params);
        break;
      }
    }

    return routed;
  }
}
