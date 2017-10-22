package com.lsjwzh.widget.text;

import android.text.Spanned;

public class StrokeSpanUtil {

  public static StrokeSpan[] getStrokeSpans(Spanned spanned) {
    return spanned.getSpans(0, spanned.length(), StrokeSpan.class);
  }

  public static void startStroke(StrokeSpan[] strokeSpans) {
    if (strokeSpans != null && strokeSpans.length > 0) {
      for (StrokeSpan span : strokeSpans) {
        span.startStroke();
      }
    }
  }

  public static void endStroke(StrokeSpan[] strokeSpans) {
    if (strokeSpans != null && strokeSpans.length > 0) {
      for (StrokeSpan span : strokeSpans) {
        span.endStroke();
      }
    }
  }

}
