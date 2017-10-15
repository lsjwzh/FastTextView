package com.lsjwzh.widget.text;

import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.IntRange;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ReplacementSpan;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * Util for TextMeasurement.
 */
public class TextMeasureUtil {

  /**
   * Do not support cross Span.
   *
   * @param text       text
   * @param parentSpan parentSpan
   * @param start      start index of parentSpan
   * @param end        end index of parentSpan
   * @param paint      TextPaint
   * @return recursive calculated width
   */
  public int recursiveGetSizeWithReplacementSpan(CharSequence text, ReplacementSpan parentSpan, @IntRange(from = 0) int start, @IntRange(from = 0) int end, Paint paint) {
    if (text instanceof Spanned) {
      Spanned spannedText = (Spanned) text;
      List<ReplacementSpan> spans = getSortedReplacementSpans(spannedText, start, end);
      if (!spans.isEmpty()) {
        int lastIndexCursor = 0;
        int width = 0;
        for (ReplacementSpan span : spans) {
          if (span == parentSpan) {
            continue;
          }
          int spanStart = spannedText.getSpanStart(span);
          int spanEnd = spannedText.getSpanEnd(span);
          width += parentSpan.getSize(paint, text, lastIndexCursor, spanStart, null);
          width += span.getSize(paint, text, spanStart, spanEnd, null);
          lastIndexCursor = spanEnd;
        }
        if (lastIndexCursor < end) {
          width += parentSpan.getSize(paint, text, lastIndexCursor, end, null);
        }
        return width;
      }
    }
    return parentSpan.getSize(paint, text, start, end, null);
  }

  public static void getTextBounds(Paint paint, CharSequence text, @IntRange(from = 0) int start, @IntRange(from = 0) int end, Rect rect) {
    char[] currentText = new char[end - start];
    TextUtils.getChars(text, start, end, currentText, 0);
    paint.getTextBounds(currentText, start, end, rect);
  }

  public static List<ReplacementSpan> getSortedReplacementSpans(final Spanned spanned, int start, int end) {
    List<ReplacementSpan> sortedSpans = new LinkedList<>();
    ReplacementSpan[] spans = spanned.getSpans(start, end, ReplacementSpan.class);
    if (spans.length > 0) {
      sortedSpans.addAll(Arrays.asList(spans));
    }
    Collections.sort(sortedSpans, new Comparator<ReplacementSpan>() {
      @Override
      public int compare(ReplacementSpan span1, ReplacementSpan span2) {
        return spanned.getSpanStart(span1) - spanned.getSpanStart(span2);
      }
    });
    return sortedSpans;
  }

}
