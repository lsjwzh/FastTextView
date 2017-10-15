package com.lsjwzh.widget.text;

import android.text.style.ReplacementSpan;

public abstract class NestReplacementSpan extends ReplacementSpan {
  private boolean mNestSpanEnabled = false;


  public boolean isNestSpanEnabled() {
    return mNestSpanEnabled;
  }

  public void setNestSpanEnabled(boolean nestSpanEnabled) {
    this.mNestSpanEnabled = nestSpanEnabled;
  }

}
