package com.lsjwzh.test;

import android.content.Context;
import android.graphics.Canvas;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by wenye on 2017/10/12.
 */

public class TestTextView extends TextView {
  private static final String TAG = TestTextView.class.getSimpleName();
  public static volatile long sDrawCost = 0;
  public static volatile long sDrawCount = 0;
  public static volatile long sMeasureCost = 0;

  public TestTextView(Context context) {
    super(context);
  }

  public TestTextView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
  }

  public TestTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    long start = SystemClock.elapsedRealtime();
    for (int i = 0; i < Const.LOOP_COUNT; i++) {
      super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
    long end = SystemClock.elapsedRealtime();
    sMeasureCost += (end - start);
//    Log.d(TAG, TAG + " onMeasure cost:" + (end - start));

  }

  @Override
  protected void onDraw(Canvas canvas) {
    long start = SystemClock.elapsedRealtime();
    for (int i = 0; i < Const.LOOP_COUNT; i++) {
      super.onDraw(canvas);
    }
    long end = SystemClock.elapsedRealtime();
    sDrawCost += (end - start);
    sDrawCount++;
//    Log.d(TAG, TAG + " onDraw cost:" + (end - start));
  }
}
