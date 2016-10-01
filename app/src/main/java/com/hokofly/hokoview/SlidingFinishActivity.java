package com.hokofly.hokoview;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.hokofly.view.SlidingFinishLayout;

public class SlidingFinishActivity extends AppCompatActivity implements SlidingFinishLayout.OnSlidingFinishListener{

    private SlidingFinishLayout mLayout = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sliding_finish);

        mLayout = (SlidingFinishLayout) findViewById(R.id.slidinglayout);
        mLayout.setOnSlidingFinishListener(this);
    }

    @Override
    public void onSlidingFinish() {
        finish();
    }
}
