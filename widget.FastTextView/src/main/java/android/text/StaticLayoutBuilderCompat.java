package android.text;

import android.annotation.Nullable;
import android.os.Build;
import android.util.Pools;

/**
 * Builder for static layouts. The builder is a newer pattern for constructing
 * StaticLayout objects and should be preferred over the constructors,
 * particularly to access newer features. To build a static layout, first
 * call {@link #obtain} with the required arguments (text, paint, and width),
 * then call setters for optional parameters, and finally {@link #build}
 * to build the StaticLayout object. Parameters not explicitly set will get
 * default values.
 */
public class StaticLayoutBuilderCompat {
  private StaticLayoutBuilderCompat() {
  }

  /**
   * Obtain a builder for constructing StaticLayout objects
   *
   * @param source The text to be laid out, optionally with spans
   * @param start  The index of the start of the text
   * @param end    The index + 1 of the end of the text
   * @param paint  The base paint used for layout
   * @param width  The width in pixels
   * @return a builder object used for constructing the StaticLayout
   */
  public static StaticLayoutBuilderCompat obtain(CharSequence source, int start, int end, TextPaint paint,
                                                 int width) {
    StaticLayoutBuilderCompat b = sPool.acquire();
    if (b == null) {
      b = new StaticLayoutBuilderCompat();
    }

    // set default initial values
    b.mText = source;
    b.mStart = start;
    b.mEnd = end;
    b.mPaint = paint;
    b.mWidth = width;
    b.mAlignment = Layout.Alignment.ALIGN_NORMAL;
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
      b.mTextDir = TextDirectionHeuristics.FIRSTSTRONG_LTR;
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      b.mBreakStrategy = Layout.BREAK_STRATEGY_SIMPLE;
      b.mHyphenationFrequency = Layout.HYPHENATION_FREQUENCY_NONE;
    }
    b.mSpacingMult = 1.0f;
    b.mSpacingAdd = 0.0f;
    b.mIncludePad = true;
    b.mEllipsizedWidth = width;
    b.mEllipsize = null;
    b.mMaxLines = Integer.MAX_VALUE;

    return b;
  }

  // release any expensive state
        /* package */ void finish() {
    mText = null;
    mPaint = null;
    mRightIndents = null;
  }

  public StaticLayoutBuilderCompat setText(CharSequence source) {
    return setText(source, 0, source.length());
  }

  /**
   * Set the text. Only useful when re-using the builder, which is done for
   * the internal implementation of {@link DynamicLayout} but not as part
   * of normal {@link StaticLayout} usage.
   *
   * @param source The text to be laid out, optionally with spans
   * @param start  The index of the start of the text
   * @param end    The index + 1 of the end of the text
   * @return this builder, useful for chaining
   */
  public StaticLayoutBuilderCompat setText(CharSequence source, int start, int end) {
    mText = source;
    mStart = start;
    mEnd = end;
    return this;
  }

  /**
   * Set the paint. Internal for reuse cases only.
   *
   * @param paint The base paint used for layout
   * @return this builder, useful for chaining
   */
  public StaticLayoutBuilderCompat setPaint(TextPaint paint) {
    mPaint = paint;
    return this;
  }

  /**
   * Set the width. Internal for reuse cases only.
   *
   * @param width The width in pixels
   * @return this builder, useful for chaining
   */
  public StaticLayoutBuilderCompat setWidth(int width) {
    mWidth = width;
    if (mEllipsize == null) {
      mEllipsizedWidth = width;
    }
    return this;
  }

  /**
   * Set the alignment. The default is {@link Layout.Alignment#ALIGN_NORMAL}.
   *
   * @param alignment Alignment for the resulting {@link StaticLayout}
   * @return this builder, useful for chaining
   */
  public StaticLayoutBuilderCompat setAlignment(Layout.Alignment alignment) {
    mAlignment = alignment;
    return this;
  }

  /**
   * Set the text direction heuristic. The text direction heuristic is used to
   * resolve text direction based per-paragraph based on the input text. The default is
   * {@link TextDirectionHeuristics#FIRSTSTRONG_LTR}.
   *
   * @param textDir text direction heuristic for resolving BiDi behavior.
   * @return this builder, useful for chaining
   */
  public StaticLayoutBuilderCompat setTextDirection(TextDirectionHeuristic textDir) {
    mTextDir = textDir;
    return this;
  }

  /**
   * Set line spacing parameters. The default is 0.0 for {@code spacingAdd}
   * and 1.0 for {@code spacingMult}.
   *
   * @param spacingAdd  line spacing add
   * @param spacingMult line spacing multiplier
   * @return this builder, useful for chaining
   * @see android.widget.TextView#setLineSpacing
   */
  public StaticLayoutBuilderCompat setLineSpacing(float spacingAdd, float spacingMult) {
    mSpacingAdd = spacingAdd;
    mSpacingMult = spacingMult;
    return this;
  }

  /**
   * Set whether to include extra space beyond font ascent and descent (which is
   * needed to avoid clipping in some languages, such as Arabic and Kannada). The
   * default is {@code true}.
   *
   * @param includePad whether to include padding
   * @return this builder, useful for chaining
   * @see android.widget.TextView#setIncludeFontPadding
   */
  public StaticLayoutBuilderCompat setIncludePad(boolean includePad) {
    mIncludePad = includePad;
    return this;
  }

  /**
   * Set the width as used for ellipsizing purposes, if it differs from the
   * normal layout width. The default is the {@code width}
   * passed to {@link #obtain}.
   *
   * @param ellipsizedWidth width used for ellipsizing, in pixels
   * @return this builder, useful for chaining
   * @see android.widget.TextView#setEllipsize
   */
  public StaticLayoutBuilderCompat setEllipsizedWidth(int ellipsizedWidth) {
    mEllipsizedWidth = ellipsizedWidth;
    return this;
  }

  /**
   * Set ellipsizing on the layout. Causes words that are longer than the view
   * is wide, or exceeding the number of lines (see #setMaxLines) in the case
   * of {@link android.text.TextUtils.TruncateAt#END} or
   * {@link android.text.TextUtils.TruncateAt#MARQUEE}, to be ellipsized instead
   * of broken. The default is
   * {@code null}, indicating no ellipsis is to be applied.
   *
   * @param ellipsize type of ellipsis behavior
   * @return this builder, useful for chaining
   * @see android.widget.TextView#setEllipsize
   */
  public StaticLayoutBuilderCompat setEllipsize(@Nullable TextUtils.TruncateAt ellipsize) {
    mEllipsize = ellipsize;
    return this;
  }

  /**
   * Set maximum number of lines. This is particularly useful in the case of
   * ellipsizing, where it changes the layout of the last line. The default is
   * unlimited.
   *
   * @param maxLines maximum number of lines in the layout
   * @return this builder, useful for chaining
   * @see android.widget.TextView#setMaxLines
   */
  public StaticLayoutBuilderCompat setMaxLines(int maxLines) {
    mMaxLines = maxLines;
    return this;
  }

  /**
   * Set break strategy, useful for selecting high quality or balanced paragraph
   * layout options. The default is {@link Layout#BREAK_STRATEGY_SIMPLE}.
   *
   * @param breakStrategy break strategy for paragraph layout
   * @return this builder, useful for chaining
   * @see android.widget.TextView#setBreakStrategy
   */
  public StaticLayoutBuilderCompat setBreakStrategy(int breakStrategy) {
    mBreakStrategy = breakStrategy;
    return this;
  }

  /**
   * Set hyphenation frequency, to control the amount of automatic hyphenation used. The
   * default is {@link Layout#HYPHENATION_FREQUENCY_NONE}.
   *
   * @param hyphenationFrequency hyphenation frequency for the paragraph
   * @return this builder, useful for chaining
   * @see android.widget.TextView#setHyphenationFrequency
   */
  public StaticLayoutBuilderCompat setHyphenationFrequency(int hyphenationFrequency) {
    mHyphenationFrequency = hyphenationFrequency;
    return this;
  }

  /**
   * Set indents. Arguments are arrays holding an indent amount, one per line, measured in
   * pixels. For lines past the last element in the array, the last element repeats.
   *
   * @param leftIndents  array of indent values for left margin, in pixels
   * @param rightIndents array of indent values for right margin, in pixels
   * @return this builder, useful for chaining
   */
  public StaticLayoutBuilderCompat setIndents(int[] leftIndents, int[] rightIndents) {
    mLeftIndents = leftIndents;
    mRightIndents = rightIndents;
    return this;
  }

  /**
   * Build the {@link StaticLayout} after options have been set.
   * <p>
   * <p>Note: the builder object must not be reused in any way after calling this
   * method. Setting parameters after calling this method, or calling it a second
   * time on the same builder object, will likely lead to unexpected results.
   *
   * @return the newly constructed {@link StaticLayout} object
   */
  public StaticLayout build() {
    StaticLayout result;
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      StaticLayout.Builder builder = StaticLayout.Builder.obtain(mText, mStart, mEnd, mPaint, mWidth);
      builder.setAlignment(mAlignment)
          .setBreakStrategy(mBreakStrategy)
          .setIndents(mLeftIndents, mRightIndents)
          .setHyphenationFrequency(mHyphenationFrequency)
          .setTextDirection(mTextDir)
          .setLineSpacing(mSpacingAdd, mSpacingMult)
          .setIncludePad(mIncludePad)
          .setEllipsizedWidth(mEllipsizedWidth)
          .setEllipsize(mEllipsize)
          .setMaxLines(mMaxLines);
      result = builder.build();
    } else {
      result = new StaticLayout(mText, mStart, mEnd, mPaint, mWidth, mAlignment, mTextDir, mSpacingMult, mSpacingAdd, mIncludePad, mEllipsize, mEllipsizedWidth, mMaxLines);
    }
    sPool.release(this);
    return result;
  }

  CharSequence mText;
  int mStart;
  int mEnd;
  TextPaint mPaint;
  int mWidth;
  Layout.Alignment mAlignment;
  TextDirectionHeuristic mTextDir;
  float mSpacingMult;
  float mSpacingAdd;
  boolean mIncludePad;
  int mEllipsizedWidth;
  TextUtils.TruncateAt mEllipsize;
  int mMaxLines = Integer.MAX_VALUE;
  int mBreakStrategy;
  int mHyphenationFrequency;
  int[] mLeftIndents;
  int[] mRightIndents;

  private static final Pools.SynchronizedPool<StaticLayoutBuilderCompat> sPool = new Pools.SynchronizedPool<>(3);
}
