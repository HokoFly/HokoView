package com.hokofly.hokoview.adapter;

import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hokofly.view.AutoScrollBanner;

/**
 * Created by HokoFly on 16/12/28.
 */

public class BannerDemoAdapter extends AutoScrollBanner.BannerPagerAdapter {
    @Override
    protected int getRealCount() {
        return 5;
    }

    @Override
    protected Object instantiateRealItem(ViewGroup container, int position) {
        TextView textView = new TextView(container.getContext());
        container.addView(textView);
        textView.setBackgroundColor(Color.parseColor("#00ff00"));
        textView.setText(position + "");
        return textView;
    }
}
