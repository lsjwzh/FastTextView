package android.text;

/**
 * Stores information about bidirectional (left-to-right or right-to-left)
 * text within the layout of a line.
 */
public class Directions {
  // Directions represents directional runs within a line of text.
  // Runs are pairs of ints listed in visual order, starting from the
  // leading margin.  The first int of each pair is the offset from
  // the first character of the line to the start of the run.  The
  // second int represents both the length and level of the run.
  // The length is in the lower bits, accessed by masking with
  // DIR_LENGTH_MASK.  The level is in the higher bits, accessed
  // by shifting by DIR_LEVEL_SHIFT and masking by DIR_LEVEL_MASK.
  // To simply test for an RTL direction, test the bit using
  // DIR_RTL_FLAG, if set then the direction is rtl.

  /* package */ int[] mDirections;

  /* package */ Directions(int[] dirs) {
    mDirections = dirs;
  }


  /* package */ static final int RUN_LENGTH_MASK = 0x03ffffff;
  /* package */ static final int RUN_LEVEL_SHIFT = 26;
  /* package */ static final int RUN_LEVEL_MASK = 0x3f;
  /* package */ static final int RUN_RTL_FLAG = 1 << RUN_LEVEL_SHIFT;
  public static final Directions DIRS_ALL_LEFT_TO_RIGHT =
      new Directions(new int[]{0, RUN_LENGTH_MASK});
  public static final Directions DIRS_ALL_RIGHT_TO_LEFT =
      new Directions(new int[]{0, RUN_LENGTH_MASK | RUN_RTL_FLAG});
}