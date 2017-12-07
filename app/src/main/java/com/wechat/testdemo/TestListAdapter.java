package com.wechat.testdemo;

import android.content.Context;
import android.os.SystemClock;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.lsjwzh.test.Util;

public abstract class TestListAdapter extends BaseAdapter {

  protected Context context;
  public long bindCost = 0;

  public TestListAdapter(Context context) {
    this.context = context;
  }

  @Override
  public int getCount() {
    return Util.TEST_LIST_ITEM_COUNT;
  }

  @Override
  public Object getItem(int position) {
    return position;
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public final View getView(int position, View convertView, ViewGroup parent) {
    long start = SystemClock.elapsedRealtime();
    convertView = bindView(position, convertView, parent);
    long end = SystemClock.elapsedRealtime();
    bindCost += (end - start);
    return convertView;
  }


  public abstract View bindView(int position, View convertView, ViewGroup parent);

}