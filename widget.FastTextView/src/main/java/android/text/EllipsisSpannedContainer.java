package android.text;

import android.text.style.ReplacementSpan;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * Fix Spanned Ellipsis bug.
 */
public class EllipsisSpannedContainer implements Spanned, GetChars{
  public static final char[] ELLIPSIS_NORMAL = { '\u2026' }; // this is "..."
  private final Spanned mSourceSpanned;
  private Layout mLayout;
  private int mEllipsisStart = -1;
  private int mEllipsisEnd = -1;
  private ReplacementSpan mCustomEllipsisSpan;

  public EllipsisSpannedContainer(Spanned spanned) {
    mSourceSpanned = spanned;
  }

  public void setCustomEllipsisSpan(ReplacementSpan customEllipsisSpan) {
    mCustomEllipsisSpan = customEllipsisSpan;
  }

  public int getEllipsisStart() {
    return mEllipsisStart;
  }

  public int getEllipsisEnd() {
    return mEllipsisEnd;
  }

  public Spanned getSourceSpanned() {
    return mSourceSpanned;
  }

  @Override
  public int getSpanEnd(Object tag) {
    if (mCustomEllipsisSpan != null && mCustomEllipsisSpan == tag) {
      return mEllipsisEnd;
    }
    return mSourceSpanned.getSpanEnd(tag);
  }

  @Override
  public int getSpanFlags(Object tag) {
    if (mCustomEllipsisSpan != null && mCustomEllipsisSpan == tag) {
      return Spanned.SPAN_INCLUSIVE_EXCLUSIVE;
    }
    return mSourceSpanned.getSpanFlags(tag);
  }

  @Override
  public int getSpanStart(Object tag) {
    if (mCustomEllipsisSpan != null && mCustomEllipsisSpan == tag) {
      return mEllipsisStart;
    }
    return mSourceSpanned.getSpanStart(tag);
  }

  @Override
  public <T> T[] getSpans(int start, int end, Class<T> type) {
    if (mEllipsisEnd >= end && mEllipsisStart <= end) {
      T[] spans1 = mSourceSpanned.getSpans(start, Math.max(mEllipsisStart, start), type);
      T[] spans2 = mSourceSpanned.getSpans(Math.min(end, mEllipsisEnd), end, type);
      int offset = mCustomEllipsisSpan != null
          && (type.isAssignableFrom(ReplacementSpan.class) || type == mCustomEllipsisSpan.getClass()) ?
          1 : 0;
      int minLen = spans1.length + spans2.length + offset;
      T[] spans = (T[]) Array.newInstance(type, minLen);
      if (spans.length > minLen) {
        spans = Arrays.copyOf(spans, minLen);
      }
      System.arraycopy(spans1, 0, spans, 0, spans1.length);
      if (offset > 0) {
        spans[spans1.length] =  (T) mCustomEllipsisSpan;
      }
      System.arraycopy(spans2, 0, spans, spans1.length + offset, spans2.length);
      return spans;
    }
    return mSourceSpanned.getSpans(start, end, type);
  }

  @Override
  public int nextSpanTransition(int start, int limit, Class type) {
    return mSourceSpanned.nextSpanTransition(start, limit, type);
  }

  @Override
  public int length() {
    return mSourceSpanned.length();
  }

  @Override
  public char charAt(int index) {
    return mSourceSpanned.charAt(index);
  }

  @Override
  public CharSequence subSequence(int start, int end) {
    return mSourceSpanned.subSequence(start, end);
  }

  @Override
  public void getChars(int start, int end, char[] dest, int destoff) {
    if (mLayout != null) {
      TextUtils.getChars(mSourceSpanned, start, end, dest, destoff);
      int line1 = mLayout.getLineForOffset(start);
      int line2 = mLayout.getLineForOffset(end);

      for (int i = line1; i <= line2; i++) {
        ellipsize(start, end, i, dest, destoff);
      }
    }
  }

  void ellipsize(int start, int end, int line, char[] dest, int destoff) {
    int ellipsisCount = mLayout.getEllipsisCount(line);

    if (ellipsisCount == 0) {
      return;
    }

    int ellipsisStart = mLayout.getEllipsisStart(line);
    int linestart = mLayout.getLineStart(line);

    for (int i = ellipsisStart; i < ellipsisStart + ellipsisCount; i++) {
      char c;

      if (i == ellipsisStart) {
        c = ELLIPSIS_NORMAL[0]; // ellipsis
        mEllipsisStart = i + linestart;
        mEllipsisEnd = mEllipsisStart + ellipsisCount;
      } else {
        c = '\uFEFF'; // 0-width space
      }

      int a = i + linestart;

      if (a >= start && a < end) {
        dest[destoff + a - start] = c;
      }
    }
  }

  public void setLayout(StaticLayout layout) {
    mLayout = layout;
  }
}
