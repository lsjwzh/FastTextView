package com.lsjwzh.test;

import android.os.SystemClock;

/**
 * Created by wenye on 2017/12/15.
 */
public class TestStats {
  private volatile long drawCost = 0;
  private volatile long drawCount = 0;
  private volatile long measureCost = 0;
  private volatile long measureCount = 0;
  private volatile long layoutCost = 0;
  private volatile long layoutCount = 0;

  private long drawStart = 0;
  private long measureStart = 0;
  private long layoutStart = 0;

  @Override
  public String toString() {
    return "TestStats{" +
        "drawCost=" + drawCost +
        ", drawCount=" + drawCount +
        ", measureCost=" + measureCost +
        ", measureCount=" + measureCount +
        ", layoutCost=" + layoutCost +
        ", layoutCount=" + layoutCount +
        '}';
  }

  public void reset() {
    drawCost = 0;
    drawCount = 0;
    measureCost = 0;
    measureCount = 0;
    layoutCost = 0;
    layoutCount = 0;
  }

  public long getDrawCost() {
    return drawCost;
  }

  public long getDrawCount() {
    return drawCount;
  }

  public long getMeasureCost() {
    return measureCost;
  }

  public long getMeasureCount() {
    return measureCount;
  }

  public long getLayoutCost() {
    return layoutCost;
  }

  public long getLayoutCount() {
    return layoutCount;
  }

  public void drawStart() {
    drawStart = SystemClock.elapsedRealtime();
  }

  public void drawEnd() {
    drawCost += SystemClock.elapsedRealtime() - drawStart;
    drawCount++;
  }

  public void measuretart() {
    measureStart = SystemClock.elapsedRealtime();
  }

  public void measureEnd() {
    measureCost += SystemClock.elapsedRealtime() - measureStart;
    measureCount++;
  }

  public void layoutStart() {
    layoutStart = SystemClock.elapsedRealtime();
  }

  public void layoutEnd() {
    layoutCost += SystemClock.elapsedRealtime() - layoutStart;
    layoutCount++;
  }
}
