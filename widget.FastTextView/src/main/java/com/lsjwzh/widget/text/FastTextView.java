package com.lsjwzh.widget.text;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.EllipsisSpannedContainer;
import android.text.Layout;
import android.text.LayoutUtils;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.StaticLayout;
import android.text.StaticLayoutBuilderCompat;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ReplacementSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;

/**
 * Simple and Fast TextView.
 */
public class FastTextView extends FastTextLayoutView {
  private static final String TAG = FastTextView.class.getSimpleName();
  private CharSequence mText;
  private TextPaint mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
  private ReplacementSpan mCustomEllipsisSpan;
  TextViewAttrsHelper mAttrsHelper = new TextViewAttrsHelper();

  public FastTextView(Context context) {
    this(context, null);
  }

  public FastTextView(Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, -1);
  }

  public FastTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context, attrs, defStyleAttr, -1);
  }

  @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
  public FastTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    init(context, attrs, defStyleAttr, defStyleRes);
  }

  private void init(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    mAttrsHelper.init(context, attrs, defStyleAttr, defStyleRes);
    setText(mAttrsHelper.mText);
    TextPaint textPaint = getTextPaint();
    textPaint.setColor(mAttrsHelper.mTextColor);
    textPaint.setTextSize(mAttrsHelper.mTextSize);
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    Layout textLayout = getTextLayout();
    if (textLayout != null) {
      CharSequence textSource = textLayout.getText();
      EllipsisSpannedContainer ellipsisSpannedContainer = null;
      if (LayoutUtils.isEllipsizer(textSource)) {
        textSource = LayoutUtils.extractFromEllipsizer(textSource);
        if (textSource instanceof EllipsisSpannedContainer) {
          ellipsisSpannedContainer = (EllipsisSpannedContainer) textSource;
          textSource = ellipsisSpannedContainer.getSourceSpanned();
        }
      }
      if (textSource instanceof Spannable) {
        if (ClickableSpanUtil.handleClickableSpan(this, textLayout, (Spannable) textSource, event)
            || ClickableSpanUtil.handleClickableSpan(this, textLayout, (Spannable) textSource,
            ClickableSpanUtil.Clickable.class, event)
            || (mCustomEllipsisSpan != null
            && mCustomEllipsisSpan instanceof ClickableSpanUtil.Clickable
            && ellipsisSpannedContainer != null
            && ClickableSpanUtil.handleClickableSpan(this, textLayout, ellipsisSpannedContainer,
            ((ClickableSpanUtil.Clickable) mCustomEllipsisSpan).getClass(), event))) {
          return true;
        }
      }
    }
    return super.onTouchEvent(event);
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    long start = System.currentTimeMillis();
    int width = MeasureSpec.getSize(widthMeasureSpec);
    if (MeasureSpec.getMode(widthMeasureSpec) != MeasureSpec.EXACTLY) {
      if (mAttrsHelper.mMaxWidth != Integer.MAX_VALUE && width > mAttrsHelper.mMaxWidth) {
        width = mAttrsHelper.mMaxWidth;
      }
    }
    if (!TextUtils.isEmpty(mText) && width > 0 &&
        (mLayout == null || width < mLayout.getWidth()
            || (width > mLayout.getWidth() && mLayout.getLineCount() > 1))) {
      mLayout = makeLayout(mText, width);
    }
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    long end = System.currentTimeMillis();
    if (BuildConfig.DEBUG) {
      Log.d(TAG, "onMeasure cost:" + (end - start));
    }
  }

  @Override
  protected void onDraw(Canvas canvas) {
    long start = System.currentTimeMillis();
    canvas.save();
    if (mLayout != null) {
      int translateX, translateY;
      int horizontalGravity = getGravity() & Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK;
      switch (horizontalGravity) {
        default:
        case Gravity.LEFT:
          translateX = getPaddingLeft();
          break;
        case Gravity.CENTER_HORIZONTAL:
          translateX = getPaddingLeft() + (getInnerWidth() - mLayout.getWidth()) / 2;
          break;
        case Gravity.RIGHT:
          translateX = getPaddingLeft() + getInnerWidth() - mLayout.getWidth();
          break;
      }
      int verticalGravity = getGravity() & Gravity.VERTICAL_GRAVITY_MASK;
      switch (verticalGravity) {
        default:
        case Gravity.TOP:
          translateY = getPaddingTop();
          break;
        case Gravity.CENTER_VERTICAL:
          translateY = getPaddingTop() + (getInnerHeight() - mLayout.getHeight()) / 2;
          break;
        case Gravity.BOTTOM:
          translateY = getPaddingTop() + getInnerHeight() - mLayout.getHeight();
          break;
      }
      canvas.translate(translateX, translateY);
      mLayout.draw(canvas);
    }
    canvas.restore();
    long end = System.currentTimeMillis();
    if (BuildConfig.DEBUG) {
      Log.d(TAG, "onDraw cost:" + (end - start));
    }
  }

  private int getInnerWidth() {
    return getWidth() - getPaddingLeft() - getPaddingRight();
  }

  private int getInnerHeight() {
    return getHeight() - getPaddingTop() - getPaddingBottom();
  }

  public TextPaint getPaint() {
    return mTextPaint;
  }

  @Deprecated
  public TextPaint getTextPaint() {
    return mTextPaint;
  }

  public void setText(@android.annotation.NonNull CharSequence text) {
    if (mText != text) {
      setTextLayout(null);
    }
    mText = text;
  }

  public CharSequence getText() {
    return mText;
  }

  /**
   * Sets the horizontal alignment of the text and the
   * vertical gravity that will be used when there is extra space
   * in the TextView beyond what is required for the text itself.
   *
   * @see android.view.Gravity
   */
  public void setGravity(int gravity) {
    if (mAttrsHelper.setGravity(gravity)) {
      setTextLayout(null);
    }
  }

  /**
   * Returns the horizontal and vertical alignment of this TextView.
   *
   * @see android.view.Gravity
   */
  public int getGravity() {
    return mAttrsHelper.getGravity();
  }

  public void setMaxWidth(int width) {
    if (mAttrsHelper.mMaxWidth != width) {
      mAttrsHelper.mMaxWidth = width;
      setTextLayout(null);
    }
  }

  public int getMaxWidth() {
    return mAttrsHelper.mMaxWidth;
  }

  public void setMaxLines(int maxLines) {
    if (mAttrsHelper.mMaxLines != maxLines) {
      mAttrsHelper.mMaxLines = maxLines;
      setTextLayout(null);
    }
  }

  public int getMaxLines() {
    return mAttrsHelper.mMaxLines;
  }

  public void setTextSize(float textSize) {
    setTextSize(textSize, TypedValue.COMPLEX_UNIT_SP);
  }

  /**
   * Set the default text size to a given unit and value.  See {@link
   * TypedValue} for the possible dimension units.
   *
   * @param textSize The desired size in the given units.
   * @param unit     The desired dimension unit.
   */
  public void setTextSize(float textSize, int unit) {
    float rawTextSize = TypedValue.applyDimension(
        unit, textSize, getResources().getDisplayMetrics());
    mTextPaint.setTextSize(rawTextSize);
    setTextLayout(null);
  }

  public float getTextSize() {
    return mTextPaint.getTextSize();
  }

  public int getEllipsize() {
    return mAttrsHelper.mEllipsize;
  }

  public void setEllipsize(int ellipsize) {
    if (mAttrsHelper.mEllipsize != ellipsize) {
      mAttrsHelper.mEllipsize = ellipsize;
      setTextLayout(null);
    }
  }

  public void setCustomEllipsisSpan(ReplacementSpan customEllipsisSpan) {
    mCustomEllipsisSpan = customEllipsisSpan;
  }

  public ReplacementSpan getCustomEllipsisSpan() {
    return mCustomEllipsisSpan;
  }

  @NonNull
  protected StaticLayout makeLayout(CharSequence text, int maxWidth) {
    int width;
    if (text instanceof Spanned) {
      width = (int) Math.ceil(Layout.getDesiredWidth(text, mTextPaint));
    } else {
      width = (int) Math.ceil(mTextPaint.measureText(text, 0, text.length()));
    }

    int layoutTargetWidth = maxWidth > 0 ? Math.min(maxWidth, width) : width;
    StaticLayoutBuilderCompat layoutBuilder = StaticLayoutBuilderCompat.obtain(text, 0, text.length(), mTextPaint, layoutTargetWidth);
    layoutBuilder.setLineSpacing(mAttrsHelper.mSpacingAdd, mAttrsHelper.mSpacingMultiplier)
        .setMaxLines(mAttrsHelper.mMaxLines)
        .setAlignment(TextViewAttrsHelper.getLayoutAlignment(this, getGravity()))
        .setIncludePad(true);
    TextUtils.TruncateAt truncateAt = getTruncateAt();
    if (truncateAt != null) {
      layoutBuilder.setEllipsize(truncateAt);
      if (width > layoutTargetWidth) {
        EllipsisSpannedContainer ellipsisSpanned =
            new EllipsisSpannedContainer(text instanceof Spanned ? (Spanned) text : new SpannableString(text));
        ellipsisSpanned.setCustomEllipsisSpan(mCustomEllipsisSpan);
        layoutBuilder.setText(ellipsisSpanned);
        if (mCustomEllipsisSpan != null) {
          layoutBuilder.setEllipsizedWidth(layoutTargetWidth - mCustomEllipsisSpan.getSize(getPaint(), mText, 0, mText.length(), null));
        }
        StaticLayout staticLayout = layoutBuilder.build();
        int lineCount = staticLayout.getLineCount();
        if (lineCount > 0) {
          int beforeLastLine = 0;
          for (int i = 0; i < lineCount - 1; i++) {
            beforeLastLine += staticLayout.getLineVisibleEnd(i);
          }
          int ellipsisCount = staticLayout.getEllipsisCount(lineCount - 1);
          int ellipsisStart = staticLayout.getEllipsisStart(lineCount - 1);
          int start = beforeLastLine + ellipsisStart;
          if (truncateAt == TextUtils.TruncateAt.END) {
            ellipsisSpanned.setEllipsisRange(start, staticLayout.getLineVisibleEnd(lineCount - 1));
          } else {
            ellipsisSpanned.setEllipsisRange(start, start + ellipsisCount);
          }
        }
        return staticLayout;
      }
    }
    return layoutBuilder.build();
  }

  protected TextUtils.TruncateAt getTruncateAt() {
    switch (mAttrsHelper.mEllipsize) {
      // do not support marque
      case 1:
        return TextUtils.TruncateAt.START;
      case 2:
        return TextUtils.TruncateAt.MIDDLE;
      case 3:
        return TextUtils.TruncateAt.END;
      default:
        return null;
    }
  }
}
