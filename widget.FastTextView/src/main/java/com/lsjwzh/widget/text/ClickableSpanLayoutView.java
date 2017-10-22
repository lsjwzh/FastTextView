package com.lsjwzh.widget.text;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.Layout;
import android.text.Spannable;
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
        if (ClickableSpanUtil.handleClickableSpan(this, textLayout, (Spannable) textSource, event)) {
          return true;
        }
      }
    }
    return super.onTouchEvent(event);
  }

}
