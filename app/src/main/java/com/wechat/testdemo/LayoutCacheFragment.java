package com.wechat.testdemo;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.StaticLayout;
import android.text.StaticLayoutBuilderCompat;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.lsjwzh.test.TestSingleLineTextView;
import com.lsjwzh.widget.text.FastTextLayoutView;
import com.lsjwzh.widget.text.FastTextView;
import com.lsjwzh.widget.text.ReadMoreTextView;
import com.lsjwzh.widget.text.StrokeSpan;

/**
 * A placeholder fragment containing a simple view.
 */
public class LayoutCacheFragment extends Fragment {

  private View mRootView;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    if (mRootView != null) {
      return mRootView;
    }
    mRootView = inflater.inflate(R.layout.layout_cache_demo, container, false);
    SpannableStringBuilder spannableStringBuilder = getSpannable();

//    FastTextLayoutView fastTextLayoutView = (FastTextLayoutView) mRootView.findViewById(R.id.fast_tv);
//    fastTextLayoutView.setTextLayout(layout);

    FastTextView fastTextView = (FastTextView) mRootView.findViewById(R.id.fast_tv2);
    fastTextView.setText(spannableStringBuilder);
//    fastTextView.setCustomEllipsisSpan(new ImageSpan(drawable));

    TextView tv = (TextView) mRootView.findViewById(R.id.system_tv);
    tv.setText(spannableStringBuilder);
//    tv.setMovementMethod(LinkMovementMethod.getInstance());
    return mRootView;
  }

  @NonNull
  private SpannableStringBuilder getSpannable() {
    SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(getResources().getString(R.string.content_cn));
    spannableStringBuilder.setSpan(new ClickableSpan() {
      @Override
      public void onClick(View widget) {
        Toast.makeText(getActivity(), "test", Toast.LENGTH_LONG).show();
      }

      @Override
      public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
//        ds.setStyle(Paint.Style.FILL_AND_STROKE);
        ds.setColor(Color.RED);
      }
    }, 0, 10, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
    Drawable drawable = getResources().getDrawable(R.mipmap.ic_launcher);
    drawable.setBounds(0, 0, 35, 35);

    spannableStringBuilder.setSpan(new ImageSpan(drawable)
        , 36, 37, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

    spannableStringBuilder.setSpan(new ImageSpan(drawable)
        , 37, 38, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

    spannableStringBuilder.setSpan(new ImageSpan(drawable)
        , 38, 39, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

    spannableStringBuilder.setSpan(new ImageSpan(drawable)
        , 39, 40, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
//    ItalicReplacementSpan italicSpan = new ItalicReplacementSpan(-0.25f);
//    StrokeSpan strokeSpan = new StrokeSpan(Color.BLUE, Color.YELLOW, 20);
//    spannableStringBuilder.setSpan(strokeSpan, 0, spannableStringBuilder.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
    return spannableStringBuilder;
  }
}
