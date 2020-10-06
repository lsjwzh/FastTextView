package com.lsjwzh.widget.text;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.StaticLayout;
import android.text.StaticLayoutBuilderCompat;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ReplacementSpan;
import android.text.util.Linkify;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class ReadMoreTextView extends FastTextView {
  public static final String ELLIPSIS_NORMAL = "\u2026"; // this is "..."
  public static final String COLLAPSE_NORMAL = "\u25b2"; // this is "▲"
  static final String TAG = ReadMoreTextView.class.getSimpleName();
  protected boolean mIsShowAll;
  protected StaticLayout mAllTextLayout;
  protected StaticLayout mWithEllipsisLayout;
  protected ReplacementSpan mCollapseSpan = new EllipsisSpan(COLLAPSE_NORMAL);

  public ReadMoreTextView(Context context) {
    this(context, null);
  }

  public ReadMoreTextView(Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, -1);
  }

  public ReadMoreTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    setCustomEllipsisSpan(new EllipsisSpan(ELLIPSIS_NORMAL));
  }

  @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
  public ReadMoreTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr,
                          int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    setCustomEllipsisSpan(new EllipsisSpan(ELLIPSIS_NORMAL));
  }

  @Override
  public void setText(CharSequence text) {
    if (text != getText()) {
      mIsShowAll = false;
      mWithEllipsisLayout = null;
      mAllTextLayout = null;
    }
    super.setText(text);
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    long start = System.currentTimeMillis();
    int width = MeasureSpec.getSize(widthMeasureSpec);
    boolean exactly = MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY;
    if (!exactly) {
      if (mAttrsHelper.mMaxWidth != Integer.MAX_VALUE && width > mAttrsHelper.mMaxWidth) {
        width = mAttrsHelper.mMaxWidth;
      }
    }
    if (width > 0) {
      width = width - getPaddingLeft() - getPaddingRight();
    }
    if (!TextUtils.isEmpty(getText()) && width > 0 &&
        (mLayout == null || width < mLayout.getWidth()
            || (width > mLayout.getWidth() && mLayout.getLineCount() > 1))) {
      mLayout = makeLayout(getText(), width, exactly);
    }
    if (mWithEllipsisLayout != null && !mIsShowAll) {
      mLayout = mWithEllipsisLayout;
      setMeasuredDimension(getMeasuredWidth(getPaddingLeft() + getPaddingRight() + mWithEllipsisLayout.getWidth(), widthMeasureSpec),
          getMeasuredHeight(getPaddingTop() + getPaddingBottom() + mWithEllipsisLayout.getHeight(), heightMeasureSpec));
    } else if (mAllTextLayout != null && mIsShowAll) {
      mLayout = mAllTextLayout;
      setMeasuredDimension(getMeasuredWidth(getPaddingLeft() + getPaddingRight() + mAllTextLayout.getWidth(), widthMeasureSpec),
          getMeasuredHeight(getPaddingTop() + getPaddingBottom() + mAllTextLayout.getHeight(),
              heightMeasureSpec));
    } else {
      super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
    long end = System.currentTimeMillis();
    if (BuildConfig.DEBUG) {
      Log.d(TAG, "onMeasure cost:" + (end - start));
    }
  }

  @Override
  protected void onDraw(Canvas canvas) {
    long start = System.currentTimeMillis();
    canvas.save();
    if (mWithEllipsisLayout != null && !mIsShowAll) {
      canvas.translate(getPaddingLeft(), getPaddingTop());
      mWithEllipsisLayout.draw(canvas);
    } else if (mAllTextLayout != null && mIsShowAll) {
      canvas.translate(getPaddingLeft(), getPaddingTop());
      mAllTextLayout.draw(canvas);
    }
    canvas.restore();
    long end = System.currentTimeMillis();
    if (BuildConfig.DEBUG) {
      Log.d(TAG, "onDraw cost:" + (end - start));
    }
  }

  @NonNull
  @Override
  protected StaticLayout makeLayout(CharSequence text, int maxWidth, boolean exactly) {
    mWithEllipsisLayout = super.makeLayout(text, maxWidth, exactly);
    SpannableStringBuilder textWithExtraEnd = new SpannableStringBuilder(text);
    if (mLinkifyMask > 0) {
      Linkify.addLinks(textWithExtraEnd, mLinkifyMask);
    }
    if (mCollapseSpan != null) {
      textWithExtraEnd.append(COLLAPSE_NORMAL);
      textWithExtraEnd.setSpan(mCollapseSpan, textWithExtraEnd.length() - 1,
          textWithExtraEnd.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
    }
    StaticLayoutBuilderCompat layoutBuilder =
        createAllStaticLayoutBuilder(textWithExtraEnd, 0, textWithExtraEnd.length(), getPaint(),
            maxWidth > 0 ? Math.min(maxWidth, mWithEllipsisLayout.getWidth()) :
                mWithEllipsisLayout.getWidth());
    layoutBuilder.setLineSpacing(mAttrsHelper.mSpacingAdd, mAttrsHelper.mSpacingMultiplier)
        .setAlignment(TextViewAttrsHelper.getLayoutAlignment(this, getGravity()))
        .setIncludePad(true);
    beforeAllStaticLayoutBuild(layoutBuilder);
    mAllTextLayout = layoutBuilder.build();
    return mWithEllipsisLayout;
  }

  @Override
  final protected StaticLayoutBuilderCompat createStaticLayoutBuilder(CharSequence source,
                                                                      int start, int end,
                                                                      TextPaint paint, int width) {
    return createEllipsisStaticLayoutBuilder(source, start, end, paint, width);
  }

  @Override
  final protected void beforeStaticLayoutBuild(StaticLayoutBuilderCompat layoutBuilder) {
    beforeEllipsisStaticLayoutBuild(layoutBuilder);
  }

  protected StaticLayoutBuilderCompat createAllStaticLayoutBuilder(CharSequence source,
                                                                   int start, int end,
                                                                   TextPaint paint, int width) {
    return StaticLayoutBuilderCompat.obtain(source, start, end, paint, width);
  }

  protected StaticLayoutBuilderCompat createEllipsisStaticLayoutBuilder(CharSequence source,
                                                                        int start, int end,
                                                                        TextPaint paint,
                                                                        int width) {
    return StaticLayoutBuilderCompat.obtain(source, start, end, paint, width);
  }

  protected void beforeAllStaticLayoutBuild(StaticLayoutBuilderCompat layoutBuilder) {
    // do noting
  }

  protected void beforeEllipsisStaticLayoutBuild(StaticLayoutBuilderCompat layoutBuilder) {
    // do noting
  }


  public void showAll() {
    mIsShowAll = true;
    mLayout = mAllTextLayout;
    requestLayout();
  }

  public void showEllipsis() {
    mIsShowAll = false;
    mLayout = mWithEllipsisLayout;
    requestLayout();
  }

  public void setCustomCollapseSpan(ReplacementSpan collapseSpan) {
    mCollapseSpan = collapseSpan;
  }

  public boolean isShowAll() {
    return mIsShowAll;
  }

  public static class EllipsisSpan extends ReplacementSpan implements ClickableSpanUtil.Clickable {
    String mText;

    public EllipsisSpan(String text) {
      mText = text;
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text, @IntRange(from = 0) int start,
                     @IntRange(from = 0) int end, float x, int top, int y, int bottom,
                     @NonNull Paint paint) {
      canvas.drawText(mText, 0, mText.length(), x, y, paint);
    }

    @Override
    public int getSize(@NonNull Paint paint, CharSequence text, @IntRange(from = 0) int start,
                       @IntRange(from = 0) int end, @Nullable Paint.FontMetricsInt fm) {
      return (int) Math.ceil(paint.measureText(mText, 0, mText.length()));
    }

    @Override
    public void onClick(View widget) {
      ReadMoreTextView readMoreTextView = (ReadMoreTextView) widget;
      if (readMoreTextView.isShowAll()) {
        readMoreTextView.showEllipsis();
      } else {
        readMoreTextView.showAll();
      }
    }
  }

}
