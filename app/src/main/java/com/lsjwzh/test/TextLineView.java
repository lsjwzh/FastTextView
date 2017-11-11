package com.lsjwzh.test;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.Directions;
import android.text.Layout;
import android.text.TextLineCompat;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Just for test.
 * Created by wenye on 2017/11/5.
 */
public class TextLineView extends View {
  private static final String TAG = "TextLineView";
  private CharSequence mText;
  private TextPaint mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
  private final Paint.FontMetricsInt mFontMetric = new Paint.FontMetricsInt();
  TextLineCompat mTextLineCompat;

  public TextLineView(Context context) {
    super(context);
  }

  public TextLineView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
  }

  public TextLineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
  public TextLineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
  }

  public void setText(CharSequence text) {
    mText = text;
    mTextPaint.setTextSize(20);
    mTextPaint.setColor(Color.RED);
    mTextLineCompat = TextLineCompat.obtain();
    mTextLineCompat.set(mTextPaint, mText, 0, mText.length(), Layout.DIR_LEFT_TO_RIGHT, Directions.DIRS_ALL_LEFT_TO_RIGHT, false, null);
    mTextPaint.getFontMetricsInt(mFontMetric);
  }

  @Override
  protected void onDraw(Canvas canvas) {
    long start = SystemClock.elapsedRealtime();
    for (int i = 0; i < Const.LOOP_COUNT; i++) {
      super.onDraw(canvas);
      int offset = getHeight() - (mFontMetric.bottom - mFontMetric.top);
      int baseLine = offset / 2 - mFontMetric.top;
      mTextLineCompat.draw(canvas, 0, 0, baseLine, baseLine + mFontMetric.bottom);
    }
    long end = SystemClock.elapsedRealtime();
    Log.d(TAG, TAG + " onDraw cost:" + (end - start));
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    long start = SystemClock.elapsedRealtime();
    for (int i = 0; i < Const.LOOP_COUNT; i++) {
      super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
    long end = SystemClock.elapsedRealtime();
    Log.d(TAG, TAG + " measure cost:" + (end - start));

  }


}
