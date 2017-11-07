package android.text;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.text.Layout.TabStops;

/**
 * Make TextLine can be accessed by other package.
 */
public class TextLineCompat implements ITextLine {
  private ITextLine mTextLine;

  private TextLineCompat(ITextLine textLine) {
    mTextLine = textLine;
  }

  /**
   * Returns a new TextLine from the shared pool.
   *
   * @return an uninitialized TextLine
   */
  public static TextLineCompat obtain() {

    if (Build.VERSION.SDK_INT >= 23) {
      return new TextLineCompat(TextLineImpl23.obtain());
    } else {
      return new TextLineCompat(TextLineImpl15.obtain());
    }
  }

  /**
   * Puts a TextLine back into the shared pool. Do not use this TextLine once
   * it has been returned.
   *
   * @param tl the textLine
   *           TextLine
   */
  public static void recycle(TextLineCompat tl) {
    if (Build.VERSION.SDK_INT >= 23) {
      TextLineImpl23.recycle((TextLineImpl23) tl.mTextLine);
    } else {
      TextLineImpl15.recycle((TextLineImpl15) tl.mTextLine);
    }
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
  @Override
  public void set(TextPaint paint, CharSequence text, int start, int limit, int dir, Directions directions, boolean hasTabs, TabStops tabStops) {
    mTextLine.set(paint, text, start, limit, dir, directions, hasTabs, tabStops);
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

  @Override
  public void draw(Canvas c, float x, int top, int y, int bottom) {
    mTextLine.draw(c, x, top, y, bottom);
  }
}
