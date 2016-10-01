package com.hokofly.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;
import android.widget.Scroller;

/**
 * 滑动结束activity
 * Created by HokoFly on 2016/6/19.
 */
public class SlidingFinishLayout extends LinearLayout {

    private static float SCREEN_WIDTH_RATIO = 0.5f;

    private static final int SCROLL_VELOCITY = 60;


    private Scroller mScroller = null;

    private int mTouchSlop;

    private float mDownX;

    private float mLastX;

    private View mParentView;

    //屏幕宽度
    private int mWidth;

    //用户不打算滑动
    private boolean mCancelled = true;

    private boolean mIsSliding = false;

    private VelocityTracker mVelocityTracker = null;

    private OnSlidingFinishListener mOnSlidingFinishListener = null;

    public SlidingFinishLayout(Context context) {
        super(context);
        initView(context);
    }

    public SlidingFinishLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);

    }

    public SlidingFinishLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);

    }

    public SlidingFinishLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);

    }


    private void initView(Context context) {
        Interpolator interpolator = new AccelerateDecelerateInterpolator();
        mScroller = new Scroller(context, interpolator);

        ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = configuration.getScaledTouchSlop();


    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        mParentView = (View) getParent();
        mWidth = getWidth();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = mLastX = ev.getRawX();
                break;
            case MotionEvent.ACTION_MOVE:

                float deltaX = mLastX - ev.getRawX();
                //滑动时屏蔽子View的点击事件
                if (Math.abs(deltaX) >= mTouchSlop) {
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }

        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {

            case MotionEvent.ACTION_DOWN:
//                addVelocityTracker(ev);

                return true;

            case MotionEvent.ACTION_MOVE:
                addVelocityTracker(ev);

                if (ev.getRawX() > mDownX) {
                    float deltaX = mLastX - ev.getRawX();
                    mLastX = ev.getRawX();
                    mParentView.scrollBy((int) deltaX, 0);
                    mIsSliding = true;
                }

                break;

            case MotionEvent.ACTION_UP:
                addVelocityTracker(ev);

                int scrollX = mParentView.getScrollX();
                if (Math.abs(scrollX) > mWidth * SCREEN_WIDTH_RATIO || getScrollVelocity() > SCROLL_VELOCITY) {// )
                    mScroller.startScroll(scrollX, 0, Math.abs(scrollX) - getWidth() + 1, 0);
                    mCancelled = false;
                } else {
                    mScroller.startScroll(scrollX, 0, -scrollX, 0);
                    mCancelled = true;
                }
                invalidate();
                recycleVelocityTracker();
                mIsSliding = false;
                break;
        }

        return super.onTouchEvent(ev);
    }

    private void addVelocityTracker(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }

        mVelocityTracker.addMovement(event);
    }

    private float getScrollVelocity() {
        mVelocityTracker.computeCurrentVelocity(1000);
        float velocity = mVelocityTracker.getXVelocity();
        return velocity;
    }

    private void recycleVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    @Override
    public void computeScroll() {

        if (mScroller.computeScrollOffset()) {
            mParentView.scrollTo(mScroller.getCurrX(), 0);
            invalidate();

            if (mScroller.isFinished()) {
                if (mOnSlidingFinishListener != null && !mIsSliding && !mCancelled) {
                    mOnSlidingFinishListener.onSlidingFinish();
                }
            }

        }

    }

    public interface OnSlidingFinishListener {
        void onSlidingFinish();
    }

    public void setOnSlidingFinishListener(OnSlidingFinishListener listener) {
        mOnSlidingFinishListener = listener;
    }

}
