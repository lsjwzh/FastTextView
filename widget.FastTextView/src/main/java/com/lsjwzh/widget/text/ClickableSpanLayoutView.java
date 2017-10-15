package com.lsjwzh.widget.text;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * FastTextLayoutView with ClickSpan support.
 */
public class ClickableSpanLayoutView extends FastTextLayoutView {

  public ClickableSpanLayoutView(Context context) {
    super(context);
  }

  public ClickableSpanLayoutView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
  }

  public ClickableSpanLayoutView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
  public ClickableSpanLayoutView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    Layout textLayout = getTextLayout();
    if (textLayout != null) {
      CharSequence textSource = textLayout.getText();
      if (textSource instanceof Spannable) {
        if (handleClickableSpan((Spannable) textSource, event)) {
          return true;
        }
      }
    }
    return super.onTouchEvent(event);
  }

  private boolean handleClickableSpan(Spannable buffer,
                                      MotionEvent event) {
    int action = event.getAction();

    if (action == MotionEvent.ACTION_UP ||
        action == MotionEvent.ACTION_DOWN) {
      int x = (int) event.getX();
      int y = (int) event.getY();

      x -= getPaddingLeft();
      y -= getPaddingTop();

      x += getScrollX();
      y += getScrollY();

      Layout layout = getTextLayout();
      int line = layout.getLineForVertical(y);
      int off = layout.getOffsetForHorizontal(line, x);

      ClickableSpan[] link = buffer.getSpans(off, off, ClickableSpan.class);

      if (link.length != 0) {
        if (action == MotionEvent.ACTION_UP) {
          link[0].onClick(this);
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
