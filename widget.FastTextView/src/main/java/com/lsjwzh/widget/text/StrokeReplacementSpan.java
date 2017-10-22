package com.lsjwzh.widget.text;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.style.ReplacementSpan;

public class StrokeReplacementSpan extends ReplacementSpan {
  private final int mStrokeColor;
  private final int mTextColor;
  private final int mStrokeWidth;
  private Rect mRect = new Rect();

  public StrokeReplacementSpan(int textColor, int strokeColor, int strokeWidth) {
    mTextColor = textColor;
    mStrokeColor = strokeColor;
    mStrokeWidth = strokeWidth;
  }

  @Override
  public void draw(@NonNull Canvas canvas, CharSequence text, @IntRange(from = 0) int start, @IntRange(from = 0) int end, float x, int top, int y, int bottom, @NonNull Paint paint) {
    canvas.save();
    canvas.translate(mStrokeWidth, 0);
    paint.setColor(mStrokeColor);
    paint.setStrokeCap(Paint.Cap.ROUND);
    paint.setStrokeWidth(mStrokeWidth);
    paint.setStyle(Paint.Style.STROKE);
    canvas.drawText(text, start, end, x, y, paint);
    paint.setColor(mTextColor);
    paint.setStyle(Paint.Style.FILL);
    canvas.drawText(text, start, end, x, y, paint);
    canvas.restore();
  }

  @Override
  public int getSize(@NonNull Paint paint, CharSequence text, @IntRange(from = 0) int start, @IntRange(from = 0) int end, @Nullable Paint.FontMetricsInt fm) {
    TextMeasureUtil.getTextBounds(paint, text, start, end, mRect);
    if (fm != null && text.length() == end - start) {
      // Extending classes can set the height of the span by updating
      // attributes of {@link android.graphics.Paint.FontMetricsInt}. If the span covers the whole
      // text, and the height is not set,
      // {@link #draw(Canvas, CharSequence, int, int, float, int, int, int, Paint)} will not be
      // called for the span.
      fm.top = mRect.top - mStrokeWidth / 2;
      fm.bottom = mRect.bottom + mStrokeWidth / 2;
    }
    return mRect.width() + mStrokeWidth * 2;
  }


}
