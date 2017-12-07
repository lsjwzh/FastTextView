package com.lsjwzh.test;

import android.os.Handler;
import android.os.Looper;
import android.widget.AbsListView;
import android.widget.ListView;

/**
 * Created by ragnarok on 15/7/22.
 */
public class AutoScrollHandler {

  private ListView listView;

  private int itemCount;

  private Handler uiHandler = new Handler(Looper.getMainLooper());

  public AutoScrollHandler(ListView listView, int itemCount) {
    this.listView = listView;
    this.itemCount = itemCount;
  }

  public void startAutoScrollDown(final Callback callback) {
    FpsCalculator.instance().startCalculate();
    final int position = listView.getAdapter().getCount() - 1;
    startPositionAndTrack(position, callback);
  }

  public void startAutoScrollUp(final Callback callback) {
    FpsCalculator.instance().startCalculate();
    startPositionAndTrack(0, callback);
  }

  private void startPositionAndTrack(final int position, final Callback callback) {
    FpsCalculator.instance().startCalculate();
    listView.smoothScrollToPosition(position);
    listView.setOnScrollListener(new AbsListView.OnScrollListener() {
      @Override
      public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int
          totalItemCount) {

      }

      @Override
      public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == SCROLL_STATE_IDLE
            && (position == 0 ? listView.getFirstVisiblePosition() == 0 :
            listView.getLastVisiblePosition() == position)) {
          listView.setOnScrollListener(null);
          final int fps = FpsCalculator.instance().stopGetAvgFPS();
          callback.callback(fps);
        }
      }
    });
  }

  public interface Callback {
    void callback(int fps);
  }
}
