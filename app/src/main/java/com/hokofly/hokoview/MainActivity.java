package com.hokofly.hokoview;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button mSlidingFinishButton;
    private Button mAutoBannerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSlidingFinishButton = (Button) findViewById(R.id.sliding_finish);
        mAutoBannerButton = (Button) findViewById(R.id.auto_scroll_banner);
        mSlidingFinishButton.setOnClickListener(this);
        mAutoBannerButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.sliding_finish:
                intent = new Intent(MainActivity.this, SlidingFinishActivity.class);
                break;
            case R.id.auto_scroll_banner:
                intent = new Intent(MainActivity.this, AutoScrollBannerActivity.class);
                break;
        }
        startActivity(intent);
    }
}
