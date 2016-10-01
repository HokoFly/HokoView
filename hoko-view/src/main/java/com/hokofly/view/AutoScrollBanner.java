package com.hokofly.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.lang.reflect.Field;

/**
 * Created by HokoFly on 16/12/27.
 */

public class AutoScrollBanner extends ViewPager {
    private final static long DEFAULT_INTERVAL = 4000;

    private boolean mIsAutoScroll = false;

    private boolean mIsStopByTouch = false;

    private long mScrollInterval;

    private Runnable mScrollRunnable;

    private BannerPagerAdapter mAdapter;

    private FixSpeedScroller mFixSpeedScroller;


    public AutoScrollBanner(Context context) {
        super(context);
        initView(context);
    }

    public AutoScrollBanner(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);

    }

    private void initView(Context context) {
        if (mScrollInterval == 0) {
            mScrollInterval = DEFAULT_INTERVAL;
        }

        setOffscreenPageLimit(5);

        setClipChildren(false);

        mScrollRunnable = new Runnable() {
            @Override
            public void run() {
                try {

                    if (mAdapter != null && mAdapter.getRealCount() != 0) {
                        int currentItem = getCurrentItem() + 1;
                        if (currentItem < 0) {
                            currentItem = 0; // 越界
                        }
                        setCurrentItem(currentItem);
                        postDelayed(this, mScrollInterval);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        mFixSpeedScroller = new FixSpeedScroller(getContext(), new LinearOutSlowInInterpolator());
        try {
            Field field = getClass().getSuperclass().getDeclaredField("mScroller");
            field.setAccessible(true);
            field.set(this, mFixSpeedScroller);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void setAdapter(BannerPagerAdapter adapter) {
        super.setAdapter(adapter);
        mAdapter = adapter;
    }

    public void setScrollInterval(long interval) {
        mScrollInterval = interval;
    }

    public void setScrollVertical(int duration) {
        if (mFixSpeedScroller != null) {
            mFixSpeedScroller.setmDuration(duration);
        }
    }


    private void startScroll() {
        if (mIsAutoScroll) {
            return;
        }

        postDelayed(mScrollRunnable, mScrollInterval);
        mIsAutoScroll = true;
    }

    private void stopScroll() {
        if (!mIsAutoScroll) {
            return;
        }
        removeCallbacks(mScrollRunnable);
        mIsAutoScroll = false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getActionMasked();
        if (action == MotionEvent.ACTION_DOWN && !mIsStopByTouch) {
            mIsStopByTouch = true;
            stopScroll();
        } else if (action == MotionEvent.ACTION_UP && mIsStopByTouch) {
            mIsStopByTouch = false;
            startScroll();
        }

        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startScroll();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopScroll();
    }

    /**
     * 无限轮播的Adapter
     */
    public static abstract class BannerPagerAdapter extends PagerAdapter {

        @Override
        public int getItemPosition(Object object) {
            if (object == null) {
                return PagerAdapter.POSITION_NONE;
            }
            return super.getItemPosition(object);
        }

        @Override
        public int getCount() {
           return Integer.MAX_VALUE;
        }

        protected abstract int getRealCount();

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

            container.removeView((View)object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            if (getRealCount() <= 0) {
                return null;
            }

            return instantiateRealItem(container, position % getRealCount());
        }

        protected abstract Object instantiateRealItem(ViewGroup container, int position);

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }



    private class FixSpeedScroller extends Scroller {

        private static final int DEFAULT_DURATION = 1500;

        private int mDuration = DEFAULT_DURATION;

        public FixSpeedScroller(Context context) {
            super(context);
        }

        public FixSpeedScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        public FixSpeedScroller(Context context, Interpolator interpolator, boolean flywheel) {
            super(context, interpolator, flywheel);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, mDuration);
        }

        public int getmDuration() {
            return mDuration;
        }

        public void setmDuration(int mDuration) {
            this.mDuration = mDuration;
        }
    }
}
