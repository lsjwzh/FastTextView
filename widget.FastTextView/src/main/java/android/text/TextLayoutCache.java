package android.text;

import java.util.Map;
import java.util.WeakHashMap;

public class TextLayoutCache<T extends Layout> {
  private Map<Spanned, T> mLayoutPool = new WeakHashMap<>();

  public synchronized void put(Spanned key, T value) {
    mLayoutPool.put(key, value);
  }

  public synchronized T get(Spanned key) {
    return mLayoutPool.get(key);
  }

}
