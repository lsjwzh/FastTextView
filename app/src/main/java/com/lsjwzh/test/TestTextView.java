package com.lsjwzh.test;

import android.content.Context;
import android.graphics.Canvas;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.annotation.Px;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by wenye on 2017/10/12.
 */

public class TestTextView extends TextView {
  private static final String TAG = TestTextView.class.getSimpleName();
  public static final TestStats TEST_STATS = new TestStats();

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
    TEST_STATS.measuretart();
    for (int i = 0; i < Const.LOOP_COUNT; i++) {
      super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
    TEST_STATS.measureEnd();
  }

  @Override
  protected void onDraw(Canvas canvas) {
    TEST_STATS.drawStart();
    for (int i = 0; i < Const.LOOP_COUNT; i++) {
      super.onDraw(canvas);
    }
    TEST_STATS.drawEnd();
//    Log.d(TAG, TAG + " onDraw cost:" + (end - start));
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
