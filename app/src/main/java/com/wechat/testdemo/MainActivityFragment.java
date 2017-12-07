package com.wechat.testdemo;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextLayoutCache;
import android.text.TextLayoutWarmer;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.lsjwzh.test.StaticLayoutManager;
import com.lsjwzh.test.TestSpan;
import com.lsjwzh.test.Util;

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

    mRootView.findViewById(R.id.prepare_layout_cache).setOnClickListener(new View.OnClickListener
        () {
      @Override
      public void onClick(View v) {
        StaticLayoutManager.sLayoutWarmer.setLayoutFactory(new TextLayoutWarmer
            .LayoutFactory<StaticLayout>() {

          @Override
          public StaticLayout makeLayout(CharSequence text) {
            // 公用TextPaint会导致fps下降？ why
            final TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
            textPaint.setTextSize(Util.fromDPtoPix(getActivity(), Util.TEXT_SIZE_DP));
            int width = (int) Layout.getDesiredWidth(text, textPaint);
            return new StaticLayout(text, textPaint,
                Math.min(width, Util.getScreenWidth(getActivity())),
                Layout.Alignment.ALIGN_NORMAL, 1.0f, 0f, true);
          }
        });
        StaticLayoutManager.sLayoutWarmer.addListener(new TextLayoutWarmer
            .WarmListener<StaticLayout>() {
          int count = 0;

          @Override
          public void onWarmComplete(CharSequence text, TextLayoutWarmer.WarmerTask<StaticLayout>
              warmerTask) {
            TextLayoutCache.STATIC_LAYOUT_CACHE.put(text, warmerTask.mLayout);
            count++;
            if (count == Util.TEST_LIST_ITEM_COUNT) {
              mRootView.post(new Runnable() {
                @Override
                public void run() {
                  Toast.makeText(getActivity(), "init layout and span finish", Toast.LENGTH_LONG)
                      .show();
                }
              });
            }
          }
        });

        for (int i = 0; i < Util.TEST_LIST_ITEM_COUNT; i++) {
          StaticLayoutManager.sLayoutWarmer.addText(TestSpan.getSpanString(i));
        }
//        new Thread(new Runnable() {
//          @Override
//          public void run() {
//            StaticLayoutManager.getInstance().initLayout(getActivity(), TestSpan.getSpanString
// (), TestSpan.getLongSpanString());
//            getActivity().runOnUiThread(new Runnable() {
//              @Override
//              public void run() {
//                Toast.makeText(getActivity(), "init layout and span finish", Toast.LENGTH_LONG)
// .show();
//              }
//            });
//          }
//        }).start();


      }
    });
    mRootView.findViewById(R.id.demo_layout_cache).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        getFragmentManager().beginTransaction()
            .replace(R.id.fragment, new StaticLayoutCacheTestFragment())
            .addToBackStack(null)
            .commitAllowingStateLoss();
      }
    });
    mRootView.findViewById(R.id.demo_fast_tv).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        getFragmentManager().beginTransaction()
            .replace(R.id.fragment, new FastTextViewListTestFragment())
            .addToBackStack(null)
            .commitAllowingStateLoss();
      }
    });
    mRootView.findViewById(R.id.demo_normal_list).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        getFragmentManager().beginTransaction()
            .replace(R.id.fragment, new NormalLayoutTestFragment())
            .addToBackStack(null)
            .commitAllowingStateLoss();
      }
    });
    TestSpan.init(getActivity());
    return mRootView;
  }


}
