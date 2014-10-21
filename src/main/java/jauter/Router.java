package jauter;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Router<T> {
  private final Map<String[], T>              patterns = new HashMap<String[], T>();
  private final Map<T,        List<String[]>> reverse  = new HashMap<T, List<String[]>>();

  //----------------------------------------------------------------------------

  public Router<T> pattern(String path, T target) {
    final String[] parts = path.replaceFirst("^/*", "").split("/");
    patterns.put(parts, target);

    List<String[]> patterns = reverse.get(target);
    if (patterns == null) {
      patterns = new ArrayList<String[]>();
      patterns.add(parts);
      reverse.put(target, patterns);
    } else {
      patterns.add(parts);
    }

    return this;
  }

  //----------------------------------------------------------------------------

  public Routed<T> route(String path) {
    final String[]            parts   = path.replaceFirst("^/*", "").split("/");
    final Map<String, String> params  = new HashMap<String, String>();
    boolean                   matched = true;

    for (final Map.Entry<String[], T> entry : patterns.entrySet()) {
      final String[] currParts = entry.getKey();
      final T        target    = entry.getValue();

      matched = true;
      params.clear();

      if (parts.length == currParts.length) {
        for (int i = 0; i < currParts.length; i++) {
          final String token = currParts[i];
          if (token.length() > 0 && token.charAt(0) == ':') {
            params.put(token.substring(1), parts[i]);
          } else if (!token.equals(parts[i])) {
            matched = false;
            break;
          }
        }
      } else if (currParts.length > 0 && currParts[currParts.length - 1].equals(":*") && parts.length >= currParts.length) {
        for (int i = 0; i < currParts.length - 1; i++) {
          final String token = currParts[i];
          if (token.length() > 0 && token.charAt(0) == ':') {
            params.put(token.substring(1), parts[i]);
          } else if (!token.equals(parts[i])) {
            matched = false;
            break;
          }
        }

        if (matched) {
          final StringBuilder b = new StringBuilder(parts[currParts.length - 1]);
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

  //----------------------------------------------------------------------------

  @SuppressWarnings("unchecked")
  public String path(T target, Object... params) {
    if (params.length == 0) return path(target, Collections.emptyMap());

    if (params.length == 1 && params[0] instanceof Map<?, ?>) return pathMap(target, (Map<Object, Object>) params[0]);

    if (params.length % 2 == 1) throw new RuntimeException("Missing value for param: " + params[params.length - 1]);

    final Map<Object, Object> map = new HashMap<Object, Object>();
    for (int i = 0; i < params.length; i += 2) {
      final String key   = params[i].toString();
      final String value = params[i + 1].toString();
      map.put(key, value);
    }
    return pathMap(target, map);
  }

  private String pathMap(T target, Map<Object, Object> params) {
    final List<String[]> patterns = (target instanceof Class<?>) ?
      getPatternsByTargetClass((Class<?>) target) : reverse.get(target);
    if (patterns == null) return null;

    try {
      // The best one is the one with minimum number of params in the query
      String bestCandidate  = null;
      int    minQueryParams = Integer.MAX_VALUE;

      boolean           matched  = true;
      final Set<String> usedKeys = new HashSet<String>();

      for (final String[] pattern : patterns) {
        matched = true;
        usedKeys.clear();

        final StringBuilder b = new StringBuilder();

        for (final String token : pattern) {
          b.append('/');

          if (token.length() > 0 && token.charAt(0) == ':') {
            final String key   = token.substring(1);
            final Object value = params.get(key);
            if (value == null) {
              matched = false;
              break;
            }

            usedKeys.add(key);
            b.append(value.toString());
          } else {
            b.append(token);
          }
        }

        if (matched) {
          final int numQueryParams = params.size() - usedKeys.size();
          if (numQueryParams < minQueryParams) {
            if (numQueryParams > 0) {
              boolean firstQueryParam = true;

              for (final Map.Entry<Object, Object> entry : params.entrySet()) {
                final String key = entry.getKey().toString();
                if (!usedKeys.contains(key)) {
                  if (firstQueryParam) {
                    b.append('?');
                    firstQueryParam = false;
                  } else {
                    b.append('&');
                  }

                  final String value = entry.getValue().toString();
                  b.append(URLEncoder.encode(key, "UTF-8"));    // May throw UnsupportedEncodingException
                  b.append('=');
                  b.append(URLEncoder.encode(value, "UTF-8"));  // May throw UnsupportedEncodingException
                }
              }
            }

            bestCandidate  = b.toString();
            minQueryParams = numQueryParams;
          }
        }
      }

      return bestCandidate;
    } catch (UnsupportedEncodingException e) {
      return null;
    }
  }

  /** @return null if there's no match */
  private List<String[]> getPatternsByTargetClass(Class<?> klass) {
    List<String[]> ret = null;
    for (Map.Entry<T, List<String[]>> entry : reverse.entrySet()) {
      T       key     = entry.getKey();
      boolean matched = false;

      if (key == klass) {
        matched = true;
      } else if (!(key instanceof Class<?>)) {
        Class<?> keyClass = key.getClass();
        if (klass.isAssignableFrom(keyClass)) matched = true;
      }

      if (matched) {
        if (ret == null) ret = new ArrayList<String[]>();
        ret.addAll(entry.getValue());
      }
    }

    return ret;
  }
}
