package jauter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Router<T> {
  private Map<String[], T>              patterns = new HashMap<String[], T>();
  private Map<T,        List<String[]>> reverse  = new HashMap<T, List<String[]>>();

  public void pattern(String path, T target) {
    String[] parts = path.split("/");
    patterns.put(parts, target);

    List<String[]> patterns = reverse.get(target);
    if (patterns == null) {
      patterns = new ArrayList<String[]>();
      patterns.add(parts);
      reverse.put(target, patterns);
    } else {
      patterns.add(parts);
    }
  }

  public Routed<T> route(String path) {
    String[]            parts   = path.split("/");
    boolean             matched = true;
    Map<String, String> params  = new HashMap<String, String>();

    for (Map.Entry<String[], T> entry : patterns.entrySet()) {
      String[] currParts = entry.getKey();
      T        target    = entry.getValue();

      matched = true;
      params.clear();

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
      } else if (currParts.length > 0 && currParts[currParts.length - 1].equals(":*") && parts.length >= currParts.length) {
        for (int i = 0; i < currParts.length - 1; i++) {
          String token = currParts[i];
          if (token.length() > 0 && token.charAt(0) == ':') {
            params.put(token.substring(1), parts[i]);
          } else if (!token.equals(parts[i])) {
            matched = false;
            break;
          }
        }

        if (matched) {
          StringBuilder b = new StringBuilder(parts[currParts.length - 1]);
          for (int i = currParts.length; i < parts.length; i++) {
            b.append('/');
            b.append(parts[i]);
          }
          params.put("*", b.toString());
        }
      } else {
        matched = false;
      }

      if (matched) return new Routed<T>(target, params);
    }

    return null;
  }

  public String path(T target, Object... params) {
    if (params.length == 0) return path(target, Collections.emptyMap());

    if (params.length % 2 == 1) throw new RuntimeException("Missing value for param: " + params[params.length - 1]);

    Map<String, Object> map = new HashMap<String, Object>();
    for (int i = 0; i < params.length; i += 2) {
      String key   = params[i].toString();
      String value = params[i + 1].toString();
      map.put(key, value);
    }
    return path(target, map);
  }

  public String path(T target, Map<String, Object> params) {
    List<String[]> patterns = reverse.get(target);
    if (patterns == null) return null;

    for (String[] pattern : patterns) {

    }

    return null;
  }
}
