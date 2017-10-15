package com.wechat.testdemo;

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
    for (int i = 0; i < 1000; i++) {
      super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
    long end = SystemClock.elapsedRealtime();
    Log.d("test", "TestTextView measure cost:" + (end - start));

  }

  @Override
  protected void onDraw(Canvas canvas) {
    long start = SystemClock.elapsedRealtime();
    for (int i = 0; i < 1000; i++) {
      super.onDraw(canvas);
    }
    long end = SystemClock.elapsedRealtime();
    Log.d("test", "TestTextView onDraw cost:" + (end - start));
  }
}
