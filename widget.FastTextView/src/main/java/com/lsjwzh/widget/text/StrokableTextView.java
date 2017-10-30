package com.lsjwzh.widget.text;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.Spanned;
import android.util.AttributeSet;
import android.util.Log;

public class StrokableTextView extends FastTextView {
  private static final String TAG = "StrokableTextView";

  public StrokableTextView(Context context) {
    super(context);
  }

  public StrokableTextView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
  }

  public StrokableTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
  public StrokableTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
  }


  @Override
  protected void onDraw(Canvas canvas) {
    long start = System.currentTimeMillis();
    CharSequence text = getText();
    if (text instanceof Spanned) {
      StrokeSpan[] strokeSpans = StrokeSpanUtil.getStrokeSpans((Spanned) text);
      if (strokeSpans != null && strokeSpans.length > 0) {
        StrokeSpanUtil.startStroke(strokeSpans);
        super.onDraw(canvas);
        StrokeSpanUtil.endStroke(strokeSpans);
      }
    }
    super.onDraw(canvas);
    long end = System.currentTimeMillis();
    if (BuildConfig.DEBUG) {
      Log.d(TAG, "onDraw cost:" + (end - start));
    }
  }
}
