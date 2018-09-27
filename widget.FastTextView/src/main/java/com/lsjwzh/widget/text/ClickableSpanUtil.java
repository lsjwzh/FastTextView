package com.lsjwzh.widget.text;

import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;

public class ClickableSpanUtil {
  public static boolean handleClickableSpan(View view, Layout layout, Spanned buffer, Class<?
      extends Clickable> spanType, MotionEvent event) {
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
      int off = getOffsetForHorizontal(view, layout, x, line);

      Clickable[] link = buffer.getSpans(off, off, spanType);

      if (link.length != 0) {
        if (action == MotionEvent.ACTION_UP) {
          link[0].onClick(view);
        } else if (buffer instanceof Spannable) {
          Selection.setSelection((Spannable) buffer,
              buffer.getSpanStart(link[0]),
              buffer.getSpanEnd(link[0]));
        }
        return true;
      } else if (buffer instanceof Spannable) {
        Selection.removeSelection((Spannable) buffer);
      }
    }

    return false;
  }

  public static boolean handleClickableSpan(View view, Layout layout, Spannable buffer,
                                            MotionEvent event) {
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
      line = Math.min(layout.getLineCount() - 1, line); // 避免line超出line count
      int off = getOffsetForHorizontal(view, layout, x, line);

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

  private static int getOffsetForHorizontal(View view, Layout layout, int x, int line) {
    if (view.getWidth() > layout.getWidth()) {
      if (view instanceof FastTextView) {
        int gravity = ((FastTextView) view).getGravity();
        int translateX;
        int horizontalGravity = gravity & Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK;
        switch (horizontalGravity) {
          default:
          case Gravity.LEFT:
            translateX = view.getPaddingLeft();
            break;
          case Gravity.CENTER_HORIZONTAL:
            translateX = view.getPaddingLeft() + (((FastTextView) view).getInnerWidth() - layout
                .getWidth()) / 2;
            break;
          case Gravity.RIGHT:
            translateX = view.getPaddingLeft() + ((FastTextView) view).getInnerWidth() - layout
                .getWidth();
            break;
        }
        x -= translateX;
      }
    }
    try {
      return layout.getOffsetForHorizontal(line, x);
    } catch (Throwable e) {
      e.printStackTrace();
    }
    return layout.getLineEnd(line);
  }

  public interface Clickable {
    /**
     * Performs the click action associated with this span.
     */
    void onClick(View widget);
  }

}
