package com.lsjwzh.widget.text;

import android.graphics.Paint;
import android.text.TextPaint;
import android.text.style.CharacterStyle;

/**
 * 描边Span,需要配合override textview的onDraw方法使用.
 */
public class StrokeSpan extends CharacterStyle {
  private final int mStrokeColor;
  private final int mTextColor;
  private final int mStrokeWidth;
  private boolean mDrawStroke = true;

  @Override
  public void updateDrawState(TextPaint tp) {
    if (mDrawStroke) {
      tp.setColor(mStrokeColor);
      tp.setStrokeCap(Paint.Cap.ROUND);
      tp.setStrokeWidth(mStrokeWidth);
      tp.setStyle(Paint.Style.STROKE);
    } else {
      tp.setColor(mTextColor);
      tp.setStyle(Paint.Style.FILL);
    }
  }

  public StrokeSpan(int textColor, int strokeColor, int strokeWidth) {
    mTextColor = textColor;
    mStrokeColor = strokeColor;
    mStrokeWidth = strokeWidth;
  }

  public void startStroke() {
    mDrawStroke = true;
  }

  public void endStroke() {
    mDrawStroke = false;
  }

  public boolean isStrokeDrawing() {
    return mDrawStroke;
  }
}

