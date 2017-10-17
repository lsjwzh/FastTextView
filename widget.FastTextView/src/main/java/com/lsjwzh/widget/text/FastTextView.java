package com.lsjwzh.widget.text;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.Layout;
import android.text.Spanned;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;

/**
 * Simple and Fast TextView.
 */
public class FastTextView extends FastTextLayoutView {
  private static final String TAG = FastTextView.class.getSimpleName();
  private CharSequence mText;
  private TextPaint mTextPaint = new TextPaint();


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
    if (BuildConfig.DEBUG) {
      Log.d(TAG, "onDraw cost:" + (end - start));
    }
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    long start = System.currentTimeMillis();
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    long end = System.currentTimeMillis();
    if (BuildConfig.DEBUG) {
      Log.d(TAG, "onMeasure cost:" + (end - start));
    }
  }

  public TextPaint getTextPaint() {
    return mTextPaint;
  }

  public void setText(CharSequence text) {
    if (TextUtils.isEmpty(text)) {
      mText = "";
      setTextLayout(null);
      return;
    }
    if (mText != text) {
      int width;
      if (text instanceof Spanned) {
        width = (int) Math.ceil(Layout.getDesiredWidth(text, mTextPaint));
      } else {
        width = (int) Math.ceil(mTextPaint.measureText(text, 0, text.length()));
      }
      StaticLayout layout = new StaticLayout(text, mTextPaint,
          width, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, true);
      setTextLayout(layout);
    }
    mText = text;
  }

  public CharSequence getText() {
    return mText;
  }

  public void setTextSize(float textSize) {
    setTextSize(textSize, TypedValue.COMPLEX_UNIT_SP);
  }


  /**
   * Set the default text size to a given unit and value.  See {@link
   * TypedValue} for the possible dimension units.
   *
   * @param textSize The desired size in the given units.
   * @param unit The desired dimension unit.
   *
   * @attr ref android.R.styleable#TextView_textSize
   */
  public void setTextSize(float textSize, int unit) {
    float rawTextSize = TypedValue.applyDimension(
        unit, textSize, getResources().getDisplayMetrics());
    mTextPaint.setTextSize(rawTextSize);
  }
}
