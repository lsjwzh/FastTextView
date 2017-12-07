package com.lsjwzh.test;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;

/**
 * A custom view for rendering layout directly.
 */
public class FastTextLayoutView extends com.lsjwzh.widget.text.ClickableSpanLayoutView {
  private static final String TAG = FastTextLayoutView.class.getSimpleName();
  private boolean mIsDebug = false;
  public static volatile long sDrawCost = 0;
  public static volatile long sDrawCount = 0;
  public static volatile long sMeasureCost = 0;

  public FastTextLayoutView(Context context) {
    super(context);
  }

  public FastTextLayoutView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
  }

  public FastTextLayoutView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
  public FastTextLayoutView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
  }

  @Override
  protected void onDraw(Canvas canvas) {
    long start = SystemClock.elapsedRealtime();
    for (int i = 0; i < Const.LOOP_COUNT; i++) {
      // TODO for test
      super.onDraw(canvas);
    }
    long end = SystemClock.elapsedRealtime();
    sDrawCost += (end - start);
    sDrawCount++;
    if (mIsDebug) {
      Log.d(TAG, TAG + " onDraw cost:" + (end - start));
    }
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    long start = SystemClock.elapsedRealtime();
    for (int i = 0; i < Const.LOOP_COUNT; i++) {
      // TODO for test
      super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
    long end = SystemClock.elapsedRealtime();
    sMeasureCost += (end - start);
    if (mIsDebug) {
      Log.d(TAG, TAG + " onMeasure cost:" + (end - start));
    }
  }
}
