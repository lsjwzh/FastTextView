package com.lsjwzh.widget.text;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
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
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.StaticLayout;
import android.text.StaticLayoutBuilderCompat;
import android.text.TextLayoutCache;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ReplacementSpan;
import android.text.util.Linkify;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import java.util.regex.Pattern;

/**
 * Simple and Fast TextView.
 */
public class FastTextView extends FastTextLayoutView {
  private static final String TAG = FastTextView.class.getSimpleName();
  TextViewAttrsHelper mAttrsHelper = new TextViewAttrsHelper();
  private CharSequence mText;
  private TextPaint mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
  private ReplacementSpan mCustomEllipsisSpan;
  private boolean mEnableLayoutCache = false; // experiment
  private EllipsisSpannedContainer mEllipsisSpanned;
  private int mCurTextColor;
  protected boolean mCompressText;
  protected int mLinkifyMask;
  private int mLinkColor = Color.parseColor("#109DD0");

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
  public FastTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int
      defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    init(context, attrs, defStyleAttr, defStyleRes);
  }

  private void init(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int
      defStyleRes) {
    mAttrsHelper.init(context, attrs, defStyleAttr, defStyleRes);
    setText(mAttrsHelper.mText);
    TextPaint textPaint = getTextPaint();
    mCurTextColor = mAttrsHelper.mTextColor.getDefaultColor();
    textPaint.setColor(mCurTextColor);
    textPaint.setTextSize(mAttrsHelper.mTextSize);
    final Resources.Theme theme = context.getTheme();
    TypedArray a = theme.obtainStyledAttributes(attrs, R.styleable.FastTextView, defStyleAttr,
        defStyleRes);
    mEnableLayoutCache = a.getBoolean(R.styleable.FastTextView_enableLayoutCache, false);
    a.recycle();
  }

  private void updateTextColors() {
    boolean inval = false;
    final int[] drawableState = getDrawableState();
    int color = mAttrsHelper.mTextColor.getColorForState(drawableState, mCurTextColor);
    if (color != mCurTextColor) {
      mCurTextColor = color;
      getTextPaint().setColor(mCurTextColor);
      inval = true;
    }
    if (inval) {
      // Text needs to be redrawn with the new color
      invalidate();
    }
  }

  @Override
  protected void drawableStateChanged() {
    super.drawableStateChanged();
    if (mAttrsHelper.mTextColor != null && mAttrsHelper.mTextColor.isStateful()) {
      updateTextColors();
    }
  }

  public void setLineSpacing(int spacingAdd, float spacingMultiplier) {
    mAttrsHelper.mSpacingAdd = spacingAdd;
    mAttrsHelper.mSpacingMultiplier = spacingMultiplier;
    clearTextLayout();
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    Layout textLayout = getTextLayout();
    if (textLayout != null) {
      CharSequence textSource = textLayout.getText();
      EllipsisSpannedContainer ellipsisSpannedContainer = null;
      if (LayoutUtils.isEllipsizer(textSource) && mEllipsisSpanned != null) {
        ellipsisSpannedContainer = mEllipsisSpanned;
        textSource = ellipsisSpannedContainer.getSourceSpanned();
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
    if (shouldResetStaticLayout(width, mText, mLayout)) {
      if (mEnableLayoutCache) {
        mLayout = TextLayoutCache.STATIC_LAYOUT_CACHE.get(mText);
        if (mLayout == null) {
          mLayout = makeLayout(mText, width, exactly);
          TextLayoutCache.STATIC_LAYOUT_CACHE.put(mText, (StaticLayout) mLayout);
        }
      } else {
        mLayout = makeLayout(mText, width, exactly);
      }
    }
    if (Build.VERSION.SDK_INT <= 19 && mLayout != null) {
      // when <= api 19, maxLines can not be supported well.
      int height = mAttrsHelper.mMaxLines < mLayout.getLineCount() ? mLayout.getLineTop
          (mAttrsHelper.mMaxLines) : mLayout.getHeight();
      setMeasuredDimension(getMeasuredWidth(getPaddingLeft() + getPaddingRight() + mLayout
              .getWidth(), widthMeasureSpec),
          getMeasuredHeight(getPaddingTop() + getPaddingBottom() + height, heightMeasureSpec));
    } else {
      super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
  }

  protected boolean shouldResetStaticLayout(int width, CharSequence text, Layout layout) {
    return !TextUtils.isEmpty(text) && width > 0 &&
        (layout == null || width < layout.getWidth()
            || (width > layout.getWidth() && layout.getLineCount() > 1)
            || (width > layout.getWidth() && getContentWidth(text) > layout.getWidth()));
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

  public int getInnerWidth() {
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

  public CharSequence getText() {
    return mText;
  }

  public void setText(@android.annotation.NonNull CharSequence text) {
    if (mText != text) {
      clearTextLayout(false);
    }
    mText = text;
  }

  private void clearTextLayout() {
    clearTextLayout(false);
  }

  private void clearTextLayout(boolean cleanCache) {
    if (mEnableLayoutCache && cleanCache) {
      TextLayoutCache.STATIC_LAYOUT_CACHE.remove(mText);
    }
    mEllipsisSpanned = null;
    setTextLayout(null);
    requestLayout();
    invalidate();
  }

  /**
   * Returns the horizontal and vertical alignment of this TextView.
   *
   * @see android.view.Gravity
   */
  public int getGravity() {
    return mAttrsHelper.getGravity();
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
      clearTextLayout();
    }
  }

  public int getMaxWidth() {
    return mAttrsHelper.mMaxWidth;
  }

  public void setMaxWidth(int width) {
    if (mAttrsHelper.mMaxWidth != width) {
      mAttrsHelper.mMaxWidth = width;
      clearTextLayout();
    }
  }

  public int getMaxLines() {
    return mAttrsHelper.mMaxLines;
  }

  public void setMaxLines(int maxLines) {
    if (mAttrsHelper.mMaxLines != maxLines) {
      mAttrsHelper.mMaxLines = maxLines;
      clearTextLayout();
    }
  }

  /**
   * Set the default text size to a given unit and value.  See {@link
   * TypedValue} for the possible dimension units.
   *
   * @param textSize The desired size in the given units.
   * @param unit The desired dimension unit.
   */
  public void setTextSize(float textSize, int unit) {
    float rawTextSize = TypedValue.applyDimension(
        unit, textSize, getResources().getDisplayMetrics());
    if (rawTextSize != mTextPaint.getTextSize()) {
      mTextPaint.setTextSize(rawTextSize);
      clearTextLayout();
    }
  }

  public float getTextSize() {
    return mTextPaint.getTextSize();
  }

  public void setTextSize(float textSize) {
    setTextSize(textSize, TypedValue.COMPLEX_UNIT_SP);
  }

  public int getEllipsize() {
    return mAttrsHelper.mEllipsize;
  }

  public void setEllipsize(int ellipsize) {
    if (mAttrsHelper.mEllipsize != ellipsize) {
      mAttrsHelper.mEllipsize = ellipsize;
      clearTextLayout();
    }
  }

  public ReplacementSpan getCustomEllipsisSpan() {
    return mCustomEllipsisSpan;
  }

  public void setCustomEllipsisSpan(ReplacementSpan customEllipsisSpan) {
    mCustomEllipsisSpan = customEllipsisSpan;
  }

  public void compressText(boolean enable) {
    mCompressText = enable;
  }

  public void addLinks(int mask) {
    mLinkifyMask = mask;
    mTextPaint.linkColor = mLinkColor;
  }

  @NonNull
  protected StaticLayout makeLayout(CharSequence text, int maxWidth, boolean exactly) {
    if (mCompressText) {
      SpannableStringBuilder ssb = new SpannableStringBuilder();
      String[] patterns = Pattern.compile("\n").split(text);
      int patternSize = patterns.length;
      int realCount = 0;
      for (int i = 0; i < patternSize; i++) {
        realCount++;
        if (patterns[i].isEmpty()) continue;
        ssb.append(patterns[i]);
        if (i < patternSize - 1) {
          ssb.append("\n");
        }
      }
      if (realCount >= mAttrsHelper.mMaxLines) {
        ssb.append("\n").append("\n"); // extra line to fix ellipse symbol display
      }
      text = ssb;
    } else {
      text = new SpannableString(text); // also make text into SpannableString.
    }

    if (mLinkifyMask > 0) {
      Linkify.addLinks((Spannable) text, mLinkifyMask);
    }

    TextUtils.TruncateAt truncateAt = getTruncateAt();
    int layoutTargetWidth = maxWidth;
    int contentWidth = maxWidth;
    if (!exactly || truncateAt != null) {
      contentWidth = getContentWidth(text);
    }
    if (!exactly) {
      layoutTargetWidth = maxWidth > 0 ? Math.min(maxWidth, contentWidth) : contentWidth;
    }

    StaticLayoutBuilderCompat layoutBuilder = createStaticLayoutBuilder(text, 0, text
        .length(), mTextPaint, layoutTargetWidth);
    layoutBuilder.setLineSpacing(mAttrsHelper.mSpacingAdd, mAttrsHelper.mSpacingMultiplier)
        .setMaxLines(mAttrsHelper.mMaxLines)
        .setAlignment(TextViewAttrsHelper.getLayoutAlignment(this, getGravity()))
        .setIncludePad(true);
    if (truncateAt != null) {
      layoutBuilder.setEllipsize(truncateAt);
      EllipsisSpannedContainer ellipsisSpanned =
          new EllipsisSpannedContainer(text instanceof Spanned ? (Spanned) text : new
              SpannableString(text));
      ellipsisSpanned.setCustomEllipsisSpan(mCustomEllipsisSpan);
      layoutBuilder.setText(ellipsisSpanned);
      if (contentWidth > layoutTargetWidth * mAttrsHelper.mMaxLines) {
        int ellipsisWithOffset = (int) mTextPaint.measureText(ReadMoreTextView.ELLIPSIS_NORMAL) - 2;

        // when StaticLayout call calculateEllipsis,
        // textWidth <= avail will cause do nothing so we should make it different with textWidth
        if (mCustomEllipsisSpan != null) {
          layoutBuilder.setEllipsizedWidth(layoutTargetWidth - mCustomEllipsisSpan.getSize
              (getPaint(), mText, 0, mText.length(), null) + ellipsisWithOffset);
        } else if (Build.VERSION.SDK_INT <= 19) {
          ReadMoreTextView.EllipsisSpan ellipsisSpan = new ReadMoreTextView.EllipsisSpan
              (ReadMoreTextView.ELLIPSIS_NORMAL);
          ellipsisSpanned.setCustomEllipsisSpan(ellipsisSpan);
          layoutBuilder.setEllipsizedWidth(layoutTargetWidth - ellipsisSpan.getSize(getPaint(),
              mText, 0, mText.length(), null) + ellipsisWithOffset);
        } else {
          layoutBuilder.setEllipsizedWidth(layoutTargetWidth);
        }
      } else {
        layoutBuilder.setEllipsizedWidth(contentWidth);
      }
      beforeStaticLayoutBuild(layoutBuilder);
      StaticLayout layout = layoutBuilder.build();
      ellipsisSpanned.setLayout(layout);
      mEllipsisSpanned = ellipsisSpanned;
      return layout;
    }
    beforeStaticLayoutBuild(layoutBuilder);
    return layoutBuilder.build();
  }

  protected StaticLayoutBuilderCompat createStaticLayoutBuilder(CharSequence source,
      int start, int end,
      TextPaint paint, int width) {
    return StaticLayoutBuilderCompat.obtain(source, start, end, paint, width);
  }

  protected void beforeStaticLayoutBuild(StaticLayoutBuilderCompat layoutBuilder) {
    // do noting
  }

  protected int getContentWidth(CharSequence text) {
    int contentWidth;
    if (text instanceof Spanned) {
      contentWidth = (int) Math.ceil(Layout.getDesiredWidth(text, mTextPaint));
    } else {
      contentWidth = (int) Math.ceil(mTextPaint.measureText(text, 0, text.length()));
    }
    return contentWidth;
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
