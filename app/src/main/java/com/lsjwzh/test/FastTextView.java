package com.lsjwzh.test;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.annotation.Px;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;

/**
 * Simple and Fast TextView.
 */
public class FastTextView extends com.lsjwzh.widget.text.FastTextView {
  private static final String TAG = FastTextView.class.getSimpleName();
  private boolean mIsDebug = false;
  public static final TestStats TEST_STATS = new TestStats();

  public FastTextView(Context context) {
    super(context);
  }

  public FastTextView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
  }

  public FastTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
  public FastTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
  }

  @Override
  protected void onDraw(Canvas canvas) {
    TEST_STATS.drawStart();
    for (int i = 0; i < Const.LOOP_COUNT; i++) {
      super.onDraw(canvas);
    }
    TEST_STATS.drawEnd();
    if (mIsDebug) {
      Log.d(TAG, TAG + " onDraw cost:" + TEST_STATS.getDrawCost());
    }
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    TEST_STATS.measuretart();
    for (int i = 0; i < Const.LOOP_COUNT; i++) {
      super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
    TEST_STATS.measureEnd();
    if (mIsDebug) {
      Log.d(TAG, TAG + " onMeasure cost:" + TEST_STATS.getMeasureCost());
    }
  }

  @Override
  public void layout(@Px int l, @Px int t, @Px int r, @Px int b) {
    TEST_STATS.layoutStart();
    for (int i = 0; i < Const.LOOP_COUNT; i++) {
      // TODO for test
      super.layout(l, t, r, b);
    }
    TEST_STATS.layoutEnd();
  }
}
