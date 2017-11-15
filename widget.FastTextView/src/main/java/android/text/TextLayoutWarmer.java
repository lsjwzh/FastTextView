package android.text;

import android.graphics.Canvas;
import android.support.annotation.NonNull;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

/**
 * Warm up Text Layout.
 */
public class TextLayoutWarmer {
  private Canvas mCanvas = new Canvas();
  private ExecutorService mExecutor = Executors.newSingleThreadExecutor(new ThreadFactory() {
    @Override
    public Thread newThread(@NonNull Runnable r) {
      return new Thread(r, "text-layout-warmer");
    }
  });

  public void warm(Layout layout) {
    layout.draw(mCanvas);
  }

  public Future<Layout> asyncWarm(final Layout layout) {
    return mExecutor.submit(new Callable<Layout>() {
      @Override
      public Layout call() throws Exception {
        warm(layout);
        return layout;
      }
    });
  }

}
