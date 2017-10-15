package com.lsjwzh.widget.text;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.style.ReplacementSpan;

/**
 * Use getTextBounds to measure Italic text.
 * Since that TextPaint.measureText can not measure Italic text correctly.
 * Note:ItalicSpan can not include other ReplacementSpan, If you do this you will get incorrect with.
 */
public class ItalicSpan extends ReplacementSpan {
  public static final float DEFAULT_ITALIC_VALUE = -0.25f;
  private Rect mRect = new Rect();
  private float mTextSkewX = DEFAULT_ITALIC_VALUE; // -0.25f is default value for Italic style.

  public ItalicSpan() {
    this(DEFAULT_ITALIC_VALUE);
  }


  public ItalicSpan(float textSkewX) {
    mTextSkewX = textSkewX;
  }

  @Override
  public void draw(@NonNull Canvas canvas, CharSequence text, @IntRange(from = 0) int start, @IntRange(from = 0) int end, float x, int top, int y, int bottom, @NonNull Paint paint) {
    canvas.save();
    float preTextSkewX = paint.getTextSkewX();
    paint.setTextSkewX(mTextSkewX);
    canvas.drawText(text, start, end, x, y, paint);
    paint.setTextSkewX(preTextSkewX);
    canvas.restore();
  }

  /**
   * Returns the width of the span. Extending classes can set the height of the span by updating
   * attributes of {@link android.graphics.Paint.FontMetricsInt}. If the span covers the whole
   * text, and the height is not set,
   * {@link #draw(Canvas, CharSequence, int, int, float, int, int, int, Paint)} will not be
   * called for the span.
   *
   * @param paint Paint instance.
   * @param text  Current text.
   * @param start Start character index for span.
   * @param end   End character index for span.
   * @param fm    Font metrics, can be null.
   * @return Width of the span.
   */
  @Override
  public int getSize(@NonNull Paint paint, CharSequence text, @IntRange(from = 0) int start, @IntRange(from = 0) int end, @Nullable Paint.FontMetricsInt fm) {
    float preTextSkewX = paint.getTextSkewX();
    paint.setTextSkewX(mTextSkewX);
    TextMeasureUtil.getTextBounds(paint, text, start, end, mRect);
    paint.setTextSkewX(preTextSkewX);
    setHeightIfNeed(text, start, end, fm);
    return mRect.width();
  }

  private void setHeightIfNeed(CharSequence text, @IntRange(from = 0) int start, @IntRange(from = 0) int end, @Nullable Paint.FontMetricsInt fm) {
    if (fm != null && text.length() == end - start) {
      // Extending classes can set the height of the span by updating
      // attributes of {@link android.graphics.Paint.FontMetricsInt}. If the span covers the whole
      // text, and the height is not set,
      // {@link #draw(Canvas, CharSequence, int, int, float, int, int, int, Paint)} will not be
      // called for the span.
      fm.top = mRect.top;
      fm.bottom = mRect.bottom;
    }
  }
}
