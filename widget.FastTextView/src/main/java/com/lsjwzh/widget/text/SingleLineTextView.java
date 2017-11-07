package com.lsjwzh.widget.text;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.BoringLayout;
import android.text.Layout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;

/**
 * Special for Single line text.
 * 1.Plain text single line rendering
 * 2.Spanned text single line rendering
 * 3.Ellipsis
 */
public class SingleLineTextView extends FastTextLayoutView {
  private CharSequence mText;
  private TextPaint mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
  TextViewAttrsHelper mAttrsHelper = new TextViewAttrsHelper();

  public SingleLineTextView(Context context) {
    this(context, null);
  }

  public SingleLineTextView(Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, -1);
  }

  public SingleLineTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context, attrs, defStyleAttr, -1);
  }

  @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
  public SingleLineTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    init(context, attrs, defStyleAttr, defStyleRes);
  }

  private void init(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    mAttrsHelper.init(context, attrs, defStyleAttr, defStyleRes);
    TextPaint textPaint = getPaint();
    textPaint.setColor(mAttrsHelper.mTextColor);
    textPaint.setTextSize(mAttrsHelper.mTextSize);
  }

  public TextPaint getPaint() {
    return mTextPaint;
  }

  /**
   * Sets the horizontal alignment of the text and the
   * vertical gravity that will be used when there is extra space
   * in the TextView beyond what is required for the text itself.
   *
   * @attr ref android.R.styleable#TextView_gravity
   * @see android.view.Gravity
   */
  public void setGravity(int gravity) {
    if (mAttrsHelper.setGravity(gravity)) {
      setTextLayout(null);
      requestLayout();
    }
  }

  /**
   * Returns the horizontal and vertical alignment of this TextView.
   *
   * @attr ref android.R.styleable#TextView_gravity
   * @see android.view.Gravity
   */
  public int getGravity() {
    return mAttrsHelper.getGravity();
  }



  public void setMaxWidth(int width) {
    if (mAttrsHelper.mMaxWidth != width) {
      mAttrsHelper.mMaxWidth = width;
      setTextLayout(null);
    }
  }

  public int getMaxWidth() {
    return mAttrsHelper.mMaxWidth;
  }

  public void setMaxLines(int maxLines) {
    if (mAttrsHelper.mMaxLines != maxLines) {
      mAttrsHelper.mMaxLines = maxLines;
      setTextLayout(null);
    }
  }

  public int getMaxLines() {
    return mAttrsHelper.mMaxLines;
  }

  public void setTextSize(float textSize) {
    setTextSize(textSize, TypedValue.COMPLEX_UNIT_SP);
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

  public float getTextSize() {
    return mTextPaint.getTextSize();
  }

  public void setText(CharSequence text) {
    mText = text;
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    if (mLayout == null && !TextUtils.isEmpty(mText)) {
      BoringLayout.Metrics fm = BoringLayout.isBoring(mText, mTextPaint);
      mLayout = BoringLayout.make(mText, mTextPaint, MeasureSpec.getSize(widthMeasureSpec),
          TextViewAttrsHelper.getLayoutAlignment(this, getGravity()), mAttrsHelper.mSpacingMultiplier, mAttrsHelper.mSpacingAdd, fm, true);
    }
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
  }


}
