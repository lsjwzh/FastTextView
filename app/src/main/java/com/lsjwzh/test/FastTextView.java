package com.lsjwzh.test;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;

/**
 * Simple and Fast TextView.
 */
public class FastTextView extends com.lsjwzh.widget.text.StrokableTextView {
  private static final String TAG = FastTextView.class.getSimpleName();
  private boolean mIsDebug = true;

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
    long start = System.currentTimeMillis();
    super.onDraw(canvas);
    long end = System.currentTimeMillis();
    if (mIsDebug) {
      Log.d(TAG, "onDraw cost:" + (end - start));
    }
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    long start = System.currentTimeMillis();
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    long end = System.currentTimeMillis();
    if (mIsDebug) {
      Log.d(TAG, "onMeasure cost:" + (end - start));
    }
  }
}
