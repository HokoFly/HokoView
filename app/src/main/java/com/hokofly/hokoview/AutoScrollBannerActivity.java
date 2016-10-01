package com.hokofly.hokoview;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.hokofly.hokoview.adapter.BannerDemoAdapter;
import com.hokofly.view.AutoScrollBanner;

public class AutoScrollBannerActivity extends AppCompatActivity {


    private AutoScrollBanner mBanner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_scroll_banner);

        mBanner = (AutoScrollBanner) findViewById(R.id.scroll_banner);

        mBanner.setAdapter(new BannerDemoAdapter());

        mBanner.setPageMargin(10);

        mBanner.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
