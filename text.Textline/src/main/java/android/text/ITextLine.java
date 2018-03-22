package android.text;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.Layout.TabStops;

public interface ITextLine {

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
  void set(TextPaint paint, CharSequence text, int start, int limit, int dir, Directions directions, boolean hasTabs, TabStops tabStops);

  /**
   * Returns metrics information for the entire line.
   *
   * @param fmi receives font metrics information, can be null
   * @return the signed width of the line
   */
  float metrics(Paint.FontMetricsInt fmi);

  /**
   * Renders the TextLine.
   *
   * @param c      the canvas to render on
   * @param x      the leading margin position
   * @param top    the top of the line
   * @param y      the baseline
   * @param bottom the bottom of the line
   */
  void draw(Canvas c, float x, int top, int y, int bottom);
}
