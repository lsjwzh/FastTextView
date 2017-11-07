package com.lsjwzh.test;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;

import com.lsjwzh.widget.text.SingleLineTextView;

/**
 * Created by wenye on 2017/11/5.
 */

public class TestSingleLineTextView extends SingleLineTextView {
  private static final String TAG = "TestSingleLineTextView";
  private boolean mIsDebug = true;

  public TestSingleLineTextView(Context context) {
    super(context);
  }

  public TestSingleLineTextView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
  }

  public TestSingleLineTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
  public TestSingleLineTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
  }


  @Override
  protected void onDraw(Canvas canvas) {
    long start = System.currentTimeMillis();
    for (int i = 0; i < Const.LOOP_COUNT; i++) {
      super.onDraw(canvas);
    }
    long end = System.currentTimeMillis();
    if (mIsDebug) {
      Log.d(TAG, TAG + " onDraw cost:" + (end - start));
    }
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    long start = System.currentTimeMillis();
    for (int i = 0; i < Const.LOOP_COUNT; i++) {
      super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
    long end = System.currentTimeMillis();
    if (mIsDebug) {
      Log.d(TAG, TAG + " onMeasure cost:" + (end - start));
    }
  }
}
