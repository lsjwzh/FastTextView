package android.text;

import com.android.internal.util.ArrayUtils;

/**
 * Fix Spanned Ellipsis bug.
 */
public class EllipsisSpannedContainer implements Spanned {
  private final Spanned mSourceSpanned;
  private int mEllipsisStart = -1;
  private int mEllipsisEnd = -1;

  public EllipsisSpannedContainer(Spanned spanned) {
    mSourceSpanned = spanned;
  }

  public void setEllipsisSpan(int ellipsisStart, int ellipsisEnd) {
    mEllipsisStart = ellipsisStart;
    mEllipsisEnd = ellipsisEnd;
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
    return mSourceSpanned.getSpanEnd(tag);
  }

  @Override
  public int getSpanFlags(Object tag) {
    return mSourceSpanned.getSpanFlags(tag);
  }

  @Override
  public int getSpanStart(Object tag) {
    return mSourceSpanned.getSpanStart(tag);
  }

  @Override
  public <T> T[] getSpans(int start, int end, Class<T> type) {
    if (mEllipsisStart >= 0 && mEllipsisStart < end) {
      T[] spans1 = mSourceSpanned.getSpans(start, Math.max(mEllipsisStart, start), type);
      T[] spans2 = mSourceSpanned.getSpans(Math.max(mEllipsisStart, start), Math.min(end, mEllipsisEnd), type);
      if (spans1 != null) {
        T[] spans = ArrayUtils.newUnpaddedArray(type, spans1.length + spans2.length);
        System.arraycopy(spans1, 0, spans, 0, spans1.length);
        System.arraycopy(spans2, 0, spans, spans1.length, spans2.length);
      }
      return spans2;
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

}
