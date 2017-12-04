package android.text;

import java.util.Map;
import java.util.WeakHashMap;

public class TextLayoutCache<T extends Layout> {
  public static TextLayoutCache<StaticLayout> STATIC_LAYOUT_CACHE = new TextLayoutCache<>();

  private Map<CharSequence, T> mLayoutPool = new WeakHashMap<>();

  public synchronized void put(CharSequence key, T value) {
    mLayoutPool.put(key, value);
  }

  public synchronized T get(CharSequence key) {
    return mLayoutPool.get(key);
  }

  public synchronized T remove(CharSequence key) {
    return mLayoutPool.remove(key);
  }

}
