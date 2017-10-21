package com.lsjwzh.widget.text;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
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
  private int mSpacingAdd;
  private float mSpacingMultiplier = 1f;
  private int mMaxWidth;

  public FastTextView(Context context) {
    this(context, null);
  }

  public FastTextView(Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, -1);
  }

  public FastTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context, attrs, defStyleAttr, -1);
  }

  @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
  public FastTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    init(context, attrs, defStyleAttr, defStyleRes);
  }

  private void init(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    final Resources.Theme theme = context.getTheme();
    TextPaint textPaint = getTextPaint();
    TypedArray a = theme.obtainStyledAttributes(attrs,
        com.android.internal.R.styleable.TextViewAppearance, defStyleAttr, defStyleRes);
    int n = a.getIndexCount();
    for (int i = 0; i < n; i++) {
      int attr = a.getIndex(i);
      switch (attr) {
        case com.android.internal.R.styleable.TextView_textColor:
          // Do not support ColorState
          textPaint.setColor(a.getColor(attr, Color.BLACK));
          break;
        case com.android.internal.R.styleable.TextView_textSize:
          textPaint.setTextSize(a.getDimensionPixelSize(attr, (int) textPaint.getTextSize()));
          break;
        case com.android.internal.R.styleable.TextView_lineSpacingExtra:
          mSpacingAdd = a.getDimensionPixelSize(attr, mSpacingAdd);
          break;
        case com.android.internal.R.styleable.TextView_lineSpacingMultiplier:
          mSpacingMultiplier = a.getFloat(attr, mSpacingMultiplier);
          break;
      }
    }

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
      setTextLayout(makeLayout(text));
    }
    mText = text;
  }

  @NonNull
  private StaticLayout makeLayout(CharSequence text) {
    int width;
    if (text instanceof Spanned) {
      width = (int) Math.ceil(Layout.getDesiredWidth(text, mTextPaint));
    } else {
      width = (int) Math.ceil(mTextPaint.measureText(text, 0, text.length()));
    }
    return new StaticLayout(text, mTextPaint, Math.min(mMaxWidth, width),
        Layout.Alignment.ALIGN_NORMAL, mSpacingMultiplier, mSpacingAdd, true);
  }

  public CharSequence getText() {
    return mText;
  }

  public void setTextSize(float textSize) {
    setTextSize(textSize, TypedValue.COMPLEX_UNIT_SP);
  }

  public void setMaxWidth(int width) {
    mMaxWidth = width;
    if (getTextLayout() != null && !TextUtils.isEmpty(mText)) {
      setTextLayout(makeLayout(mText));
    }
  }

  /**
   * Set the default text size to a given unit and value.  See {@link
   * TypedValue} for the possible dimension units.
   *
   * @param textSize The desired size in the given units.
   * @param unit     The desired dimension unit.
   */
  public void setTextSize(float textSize, int unit) {
    float rawTextSize = TypedValue.applyDimension(
        unit, textSize, getResources().getDisplayMetrics());
    mTextPaint.setTextSize(rawTextSize);
  }
}
