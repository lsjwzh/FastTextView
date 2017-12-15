package com.wechat.testdemo;

import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lsjwzh.test.AutoScrollHandler;
import com.lsjwzh.test.FastTextView;
import com.lsjwzh.test.TestSpan;
import com.lsjwzh.test.TestTextView;
import com.lsjwzh.test.Util;

public class FastTextViewListTestFragment extends Fragment {

  private ListView listView;

  private ListAdapter adapter;

  private AutoScrollHandler autoScrollHandler;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
      Bundle savedInstanceState) {
    View viewRoot = inflater.inflate(R.layout.normal_layout_ui, container, false);

    listView = (ListView) viewRoot.findViewById(R.id.test_list);

    adapter = new ListAdapter(getActivity());

    listView.setAdapter(adapter);

    autoScrollHandler = new AutoScrollHandler(listView, Util.TEST_LIST_ITEM_COUNT);

    viewRoot.findViewById(R.id.scroll_down_button).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        FastTextView.TEST_STATS.reset();
        adapter.bindCost = 0;
        autoScrollHandler.startAutoScrollDown(new AutoScrollHandler.Callback() {
          @Override
          public void callback(int fps) {
            Toast.makeText(listView.getContext(), "Average FPS: " + fps, Toast.LENGTH_LONG).show();
            Log.e("drawFps", "TestFastTextView.avgFps" + fps);
            Log.e("bindCost", "bindCost" + adapter.bindCost);
            Log.e("TestFastTextViewStats", FastTextView.TEST_STATS.toString());
          }
        });
      }
    });

    viewRoot.findViewById(R.id.scroll_up_button).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        FastTextView.TEST_STATS.reset();
        adapter.bindCost = 0;
        autoScrollHandler.startAutoScrollUp(new AutoScrollHandler.Callback() {
          @Override
          public void callback(int fps) {
            Toast.makeText(listView.getContext(), "Average FPS: " + fps, Toast.LENGTH_LONG).show();
            Log.e("drawFps", "TestFastTextView.avgFps" + fps);
            Log.e("bindCost", "bindCost" + adapter.bindCost);
            Log.e("TestFastTextViewStats", FastTextView.TEST_STATS.toString());
          }
        });
      }
    });
    return viewRoot;
  }

  private static class ListAdapter extends TestListAdapter {

    private ListAdapter(Context context) {
      super(context);
    }

    @Override
    public View bindView(int position, View convertView, ViewGroup parent) {

      if (convertView == null) {
        convertView = LayoutInflater.from(context).inflate(R.layout.fast_list_item, parent,
            false);

        ViewHolder viewHolder = new ViewHolder();
        viewHolder.textView = (FastTextView) convertView.findViewById(R.id.fast_text_view);

        convertView.setTag(viewHolder);
      }

      ViewHolder holder = (ViewHolder) convertView.getTag();
      holder.textView.setText(TestSpan.getSpanString(position));
      return convertView;
    }

    private class ViewHolder {
      FastTextView textView;
    }
  }
}
