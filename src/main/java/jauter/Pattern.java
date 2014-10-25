package jauter;

public class Pattern<T> {
  private final String   path;
  private final String[] tokens;
  private final T        target;

  public static String removeSlashAtBothEnds(String path) {
    if (path.isEmpty()) return path;

    int beginIndex = 0;
    while (beginIndex < path.length() && path.charAt(beginIndex) == '/') beginIndex++;
    if (beginIndex == path.length()) return "";

    int endIndex = path.length() - 1;
    while (endIndex > beginIndex && path.charAt(endIndex) == '/') endIndex--;

    return path.substring(beginIndex, endIndex + 1);
  }

  public Pattern(String path, T target) {
    this.path   = removeSlashAtBothEnds(path);
    this.tokens = this.path.split("/");
    this.target = target;
  }

  public String path() {
    return path;
  }

  public String[] tokens() {
    return tokens;
  }

  public T target() {
    return target;
  }
}
