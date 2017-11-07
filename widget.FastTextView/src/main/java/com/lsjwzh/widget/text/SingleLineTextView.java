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

/**
 * Special for Single line text.
 * 1.Plain text single line rendering
 * 2.Spanned text single line rendering
 * 3.Ellipsis
 * 4.
 */
public class SingleLineTextView extends FastTextLayoutView {
  private CharSequence mText;
  private TextPaint mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);

  public SingleLineTextView(Context context) {
    super(context);
  }

  public SingleLineTextView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
  }

  public SingleLineTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
  public SingleLineTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
  }

  public void setText(CharSequence text) {
    mText = text;
    mTextPaint.setTextSize(20);
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
          Layout.Alignment.ALIGN_NORMAL, 1f, 0f, fm, true);
    }
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
  }
}
