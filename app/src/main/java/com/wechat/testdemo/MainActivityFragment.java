package com.wechat.testdemo;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.StaticLayout;
import android.text.StaticLayoutBuilderCompat;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.lsjwzh.widget.text.FastTextLayoutView;
import com.lsjwzh.widget.text.FastTextView;
import com.lsjwzh.widget.text.StrokeSpan;
import com.lsjwzh.widget.text.TextMeasureUtil;

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
    SpannableStringBuilder spannableStringBuilder = getSpannable();

    FastTextLayoutView fastTextLayoutView = (FastTextLayoutView) mRootView.findViewById(R.id.fast_tv);
    TextPaint textPaint = new TextPaint();
    textPaint.setAntiAlias(true);
    textPaint.setColor(Color.WHITE);
    float textSize = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP, 20, getResources().getDisplayMetrics());
    textPaint.setTextSize(textSize);
//    spannableStringBuilder.getSpans(0, )
    Rect bounds = new Rect();
    String text = spannableStringBuilder.toString();
    long start = SystemClock.elapsedRealtime();
    for (int i = 0; i < 1000; i++) {
      textPaint.getTextBounds(text, 0, spannableStringBuilder.length(), bounds);
    }
    long end = SystemClock.elapsedRealtime();
    float withWithTextBounds = bounds.width();
    Log.d("test", "withWithTextBounds:" + withWithTextBounds + " offset：" + 0.5f * textSize + " cost:" + (end - start));

    start = SystemClock.elapsedRealtime();
    float withWithMeasureText = 0;
    for (int i = 0; i < 1000; i++) {
      withWithMeasureText = textPaint.measureText(spannableStringBuilder, 0, spannableStringBuilder.length());
    }
    end = SystemClock.elapsedRealtime();
    Log.d("test", "withWithMeasureText:" + withWithMeasureText + " cost:" + (end - start));

    start = SystemClock.elapsedRealtime();
    float withWithDesiredWidth = 0;
    for (int i = 0; i < 1000; i++) {
      withWithDesiredWidth = Layout.getDesiredWidth(spannableStringBuilder, textPaint);
    }
    end = SystemClock.elapsedRealtime();
    Log.d("test", "withWithDesiredWidth:" + withWithDesiredWidth + " cost:" + (end - start));


    start = SystemClock.elapsedRealtime();
    for (int i = 0; i < 1000; i++) {
      spannableStringBuilder.getSpans(0, spannableStringBuilder.length(), StrokeSpan.class);
    }
    end = SystemClock.elapsedRealtime();
    Log.d("test", "getSpans cost:" + (end - start));

    // (int) (Math.ceil(withWithDesiredWidth) + 0.5f * textSize);//
    int width = (int) Math.ceil(Math.max(Math.max(withWithTextBounds, withWithMeasureText), withWithDesiredWidth));
//    StaticLayout layout = new StaticLayout(spannableStringBuilder, textPaint,
//        Math.min(width, getResources().getDisplayMetrics().widthPixels), Layout.Alignment.ALIGN_NORMAL,
//        1.0f, 0.0f, true);
    StaticLayout layout = StaticLayoutBuilderCompat.obtain(spannableStringBuilder, 0, spannableStringBuilder.length(),
        textPaint, Math.min(width, getResources().getDisplayMetrics().widthPixels))
        .setAlignment(Layout.Alignment.ALIGN_NORMAL)
        .setLineSpacing(0f, 1f)
        .setEllipsize(TextUtils.TruncateAt.END)
        .setMaxLines(2).setIncludePad(true).build();

    fastTextLayoutView.setTextLayout(layout);

    TextPaint paint = new TextPaint();
    paint.setStrokeCap(Paint.Cap.ROUND);
    paint.setStrokeWidth(2);
    paint.setStyle(Paint.Style.FILL_AND_STROKE);
    Rect rect = new Rect();
    TextMeasureUtil.getTextBounds(paint, text, 0, 10, rect);
    Log.d("test", "stroke getTextBounds:" + rect.width());
    float measureTextWidth = paint.measureText(text, 0, 10);
    Log.d("test", "stroke measureText:" + measureTextWidth);
    paint.setStyle(Paint.Style.FILL);
    TextMeasureUtil.getTextBounds(paint, text, 0, 10, rect);
    Log.d("test", "fill getTextBounds:" + rect.width());
    measureTextWidth = paint.measureText(text, 0, 10);
    Log.d("test", "fill measureText:" + measureTextWidth);

    FastTextView fastTextView = (FastTextView) mRootView.findViewById(R.id.fast_tv2);
    fastTextView.setText(spannableStringBuilder);

    TextView tv = (TextView) mRootView.findViewById(R.id.system_tv);
    tv.setText(spannableStringBuilder);
    tv.setMovementMethod(LinkMovementMethod.getInstance());
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
    }, 2, 13, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
    Drawable drawable = getResources().getDrawable(R.mipmap.ic_launcher);
    drawable.setBounds(0, 0, 35, 35);
    spannableStringBuilder.setSpan(new ImageSpan(drawable)
        , 1, 2, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
//    ItalicReplacementSpan italicSpan = new ItalicReplacementSpan(-0.25f);
//    StrokeSpan strokeSpan = new StrokeSpan(Color.BLUE, Color.YELLOW, 20);
//    spannableStringBuilder.setSpan(strokeSpan, 0, spannableStringBuilder.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
    return spannableStringBuilder;
  }
}
