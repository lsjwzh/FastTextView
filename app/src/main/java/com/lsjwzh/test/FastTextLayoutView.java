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
 * A custom view for rendering layout directly.
 */
public class FastTextLayoutView extends com.lsjwzh.widget.text.ClickableSpanLayoutView {
  private static final String TAG = FastTextLayoutView.class.getSimpleName();
  private boolean mIsDebug = false;
  public static final TestStats TEST_STATS = new TestStats();

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
    TEST_STATS.drawStart();
    for (int i = 0; i < Const.LOOP_COUNT; i++) {
      // TODO for test
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
      // TODO for test
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
