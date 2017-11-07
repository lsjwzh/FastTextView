package android.text;

/**
 * TODO.
 */
public class SimpleLayout extends Layout {

  protected SimpleLayout(CharSequence text, TextPaint paint, int width, Alignment align, float spacingMult, float spacingAdd) {
    super(text, paint, width, align, spacingMult, spacingAdd);
  }

  protected SimpleLayout(CharSequence text, TextPaint paint, int width, Alignment align, TextDirectionHeuristic textDir, float spacingMult, float spacingAdd) {
    super(text, paint, width, align, textDir, spacingMult, spacingAdd);
  }

  @Override
  public int getBottomPadding() {
    return 0;
  }

  @Override
  public int getEllipsisCount(int line) {
    return 0;
  }

  @Override
  public int getEllipsisStart(int line) {
    return 0;
  }

  @Override
  public boolean getLineContainsTab(int line) {
    return false;
  }

  @Override
  public int getLineCount() {
    return 0;
  }

  @Override
  public int getLineDescent(int line) {
    return 0;
  }

  @Override
  public Directions getLineDirections(int line) {
    return null;
  }

  @Override
  public int getLineStart(int line) {
    return 0;
  }

  @Override
  public int getLineTop(int line) {
    return 0;
  }

  @Override
  public int getParagraphDirection(int line) {
    return 0;
  }

  @Override
  public int getTopPadding() {
    return 0;
  }


  /* package */ static final int RUN_LENGTH_MASK = 0x03ffffff;
  /* package */ static final int RUN_LEVEL_SHIFT = 26;
  /* package */ static final int RUN_RTL_FLAG = 1 << RUN_LEVEL_SHIFT;

  /* package */ public static final Layout.Directions DIRS_ALL_LEFT_TO_RIGHT =
      new Layout.Directions(new int[] { 0, RUN_LENGTH_MASK });
  /* package */ public static final Layout.Directions DIRS_ALL_RIGHT_TO_LEFT =
      new Layout.Directions(new int[] { 0, RUN_LENGTH_MASK | RUN_RTL_FLAG });
}
