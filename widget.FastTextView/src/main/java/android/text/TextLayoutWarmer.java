package android.text;

import android.graphics.Canvas;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Warm up Text Layout.
 */
public class TextLayoutWarmer<T extends Layout> {
  private Canvas mCanvas = new Canvas();
  private LayoutFactory<T> mLayoutFactory;
  private Map<CharSequence, WarmerTask<T>> mLayoutPool = Collections.synchronizedMap(new
      WeakHashMap<CharSequence, WarmerTask<T>>());
  private LayoutWarmerExecutor mWarmerExecutor = new LayoutWarmerExecutor() {
    ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    public void queue(WarmerTask warmerTask) {
      executor.submit(warmerTask);
    }
  };
  private final Set<WarmListener<T>> mWarmListeners = Collections.synchronizedSet(new
      LinkedHashSet<WarmListener<T>>());

  public void setLayoutFactory(LayoutFactory<T> layoutFactory) {
    mLayoutFactory = layoutFactory;
  }

  public LayoutFactory<T> getLayoutFactory() {
    return mLayoutFactory;
  }

  public void setWarmerExecutor(LayoutWarmerExecutor warmerExecutor) {
    mWarmerExecutor = warmerExecutor;
  }

  public LayoutWarmerExecutor getWarmerExecutor() {
    return mWarmerExecutor;
  }

  public boolean contains(CharSequence text) {
    return mLayoutPool.containsKey(text);
  }

  public T getLayout(CharSequence text) {
    WarmerTask<T> tWarmerTask = mLayoutPool.get(text);
    return tWarmerTask != null ? tWarmerTask.mLayout : null;
  }

  public void addListener(WarmListener<T> listener) {
    mWarmListeners.add(listener);
  }

  public void removeListener(WarmListener<T> listener) {
    mWarmListeners.remove(listener);
  }

  public Set<WarmListener<T>> getAllListeners() {
    return mWarmListeners;
  }

  public void addText(CharSequence text) {
    if (mLayoutFactory == null) {
      throw new IllegalStateException("LayoutFactory can not be null");
    }
    if (mWarmerExecutor == null) {
      throw new IllegalStateException("WarmerExecutor can not be null");
    }
    if (contains(text)) {
      return;
    }
    WarmerTask<T> warmerTask = new WarmerTask<>(text, this);
    mLayoutPool.put(text, warmerTask);
    mWarmerExecutor.queue(warmerTask);
  }

  public void removeCache(CharSequence text) {
    WarmerTask<T> tWarmerTask = mLayoutPool.get(text);
    if (tWarmerTask != null) {
      tWarmerTask.mIsCancelled = true;
    }
    mLayoutPool.remove(text);
  }

  public interface LayoutWarmerExecutor {
    void queue(WarmerTask warmerTask);
  }

  public static class WarmerTask<T extends Layout> implements Runnable {
    public CharSequence mText;
    public TextLayoutWarmer<T> mTextLayoutWarmer;
    public T mLayout;
    public boolean mIsCancelled;

    WarmerTask(CharSequence text, TextLayoutWarmer<T> warmer) {
      mText = text;
      mTextLayoutWarmer = warmer;
    }

    @Override
    public void run() {
      if (mIsCancelled) {
        return;
      }
      mLayout = mTextLayoutWarmer.getLayoutFactory().makeLayout(mText);
      if (mLayout != null) {
        mLayout.draw(mTextLayoutWarmer.mCanvas);
        Set<WarmListener<T>> allListeners = mTextLayoutWarmer.getAllListeners();
        for (WarmListener<T> listener : allListeners) {
          listener.onWarmComplete(mText, this);
        }
      }
    }
  }

  public interface WarmListener<T extends Layout> {
    void onWarmComplete(CharSequence text, WarmerTask<T> warmerTask);
  }

  public interface LayoutFactory<T extends Layout> {
    T makeLayout(CharSequence text);
  }
}
