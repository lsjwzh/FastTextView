package com.lsjwzh.widget.text;

import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.style.ClickableSpan;
import android.view.MotionEvent;
import android.view.View;

public class ClickableSpanUtil {
  public interface Clickable {
    /**
     * Performs the click action associated with this span.
     */
    void onClick(View widget);
  }

  public static boolean handleClickableSpan(View view, Layout layout, Spannable buffer, Class<? extends Clickable> spanType, MotionEvent event) {
    int action = event.getAction();

    if (action == MotionEvent.ACTION_UP ||
        action == MotionEvent.ACTION_DOWN) {
      int x = (int) event.getX();
      int y = (int) event.getY();

      x -= view.getPaddingLeft();
      y -= view.getPaddingTop();

      x += view.getScrollX();
      y += view.getScrollY();

      int line = layout.getLineForVertical(y);
      int off = layout.getOffsetForHorizontal(line, x);

      Clickable[] link = buffer.getSpans(off, off, spanType);

      if (link.length != 0) {
        if (action == MotionEvent.ACTION_UP) {
          link[0].onClick(view);
        } else {
          Selection.setSelection(buffer,
              buffer.getSpanStart(link[0]),
              buffer.getSpanEnd(link[0]));
        }
        return true;
      } else {
        Selection.removeSelection(buffer);
      }
    }

    return false;
  }

  public static boolean handleClickableSpan(View view, Layout layout, Spannable buffer, MotionEvent event) {
    int action = event.getAction();

    if (action == MotionEvent.ACTION_UP ||
        action == MotionEvent.ACTION_DOWN) {
      int x = (int) event.getX();
      int y = (int) event.getY();

      x -= view.getPaddingLeft();
      y -= view.getPaddingTop();

      x += view.getScrollX();
      y += view.getScrollY();

      int line = layout.getLineForVertical(y);
      int off = layout.getOffsetForHorizontal(line, x);

      ClickableSpan[] link = buffer.getSpans(off, off, ClickableSpan.class);

      if (link.length != 0) {
        if (action == MotionEvent.ACTION_UP) {
          link[0].onClick(view);
        } else {
          Selection.setSelection(buffer,
              buffer.getSpanStart(link[0]),
              buffer.getSpanEnd(link[0]));
        }
        return true;
      } else {
        Selection.removeSelection(buffer);
      }
    }

    return false;
  }

}
