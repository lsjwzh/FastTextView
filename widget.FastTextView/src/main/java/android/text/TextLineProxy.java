package android.text;

import android.annotation.NonNull;
import android.graphics.Paint;

/**
 * Make TextLine can be accessed by other package.
 */
public class TextLineProxy {
  private TextLine mTextLine;

  private TextLineProxy(@NonNull TextLine textLine) {
    mTextLine = textLine;
  }

  /**
   * Returns a new TextLine from the shared pool.
   *
   * @return an uninitialized TextLine
   */
  public static TextLineProxy obtain() {
    return new TextLineProxy(TextLine.obtain());
  }

  /**
   * Puts a TextLine back into the shared pool. Do not use this TextLine once
   * it has been returned.
   *
   * @param tl the textLine
   *           TextLine
   */
  public static void recycle(TextLineProxy tl) {
    TextLine.recycle(tl.mTextLine);
  }

  /**
   * Initializes a TextLine and prepares it for use.
   *
   * @param paint      the base paint for the line
   * @param text       the text, can be Styled
   * @param start      the start of the line relative to the text
   * @param limit      the limit of the line relative to the text
   * @param dir        the paragraph direction of this line
   * @param directions the directions information of this line
   */
  public void set(TextPaint paint, CharSequence text, int start, int limit, int dir,
           Layout.Directions directions) {
    mTextLine.set(paint, text, start, limit, dir, directions, false, null);
  }

  /**
   * Returns metrics information for the entire line.
   *
   * @param fmi receives font metrics information, can be null
   * @return the signed width of the line
   */
  public float metrics(Paint.FontMetricsInt fmi) {
    return mTextLine.metrics(fmi);
  }
}
