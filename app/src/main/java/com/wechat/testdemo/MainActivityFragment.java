package com.wechat.testdemo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

  private View mRootView;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    if (mRootView != null) {
      return mRootView;
    }
    mRootView = inflater.inflate(R.layout.fragment_main, container, false);
    mRootView.findViewById(R.id.demo_measure_test).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        getFragmentManager().beginTransaction()
            .replace(R.id.fragment, new EllipseFragment())
            .addToBackStack(null)
            .commit();
      }
    });
    mRootView.findViewById(R.id.demo_ellipsis).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        getFragmentManager().beginTransaction()
            .replace(R.id.fragment, new EllipseFragment())
            .addToBackStack(null)
            .commitAllowingStateLoss();
      }
    });
    mRootView.findViewById(R.id.demo_read_more).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        getFragmentManager().beginTransaction()
            .replace(R.id.fragment, new ReadMoreFragment())
            .addToBackStack(null)
            .commitAllowingStateLoss();
      }
    });
    mRootView.findViewById(R.id.demo_layout_cache).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        getFragmentManager().beginTransaction()
            .replace(R.id.fragment, new LayoutCacheFragment())
            .addToBackStack(null)
            .commitAllowingStateLoss();
      }
    });
    return mRootView;
  }
}
