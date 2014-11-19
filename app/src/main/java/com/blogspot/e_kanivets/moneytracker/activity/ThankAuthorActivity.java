package com.blogspot.e_kanivets.moneytracker.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.util.AppUtils;
import com.vungle.publisher.EventListener;
import com.vungle.publisher.VunglePub;

/**
 * Created by fess on 16.11.2014.
 */
public class ThankAuthorActivity extends Activity {

    // get the VunglePub instance
    final VunglePub vunglePub = VunglePub.getInstance();

    private ProgressBar progressBar;

    private Activity activity;

    private boolean shouldPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_thank_author);

        activity = this;
        shouldPlay = false;

        // initialize the Publisher SDK
        final String app_id = "com.blogspot.e_kanivets.moneytracker";
        vunglePub.init(this, app_id);

        final TextView tvContribution = (TextView) findViewById(R.id.tv_contribution);
        Button btnWatchVideoAd = (Button) findViewById(R.id.btn_watch_video_ad);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        tvContribution.setText(Integer.toString(AppUtils.getContribution(activity)));

        btnWatchVideoAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(vunglePub.isCachedAdAvailable()) {
                    if(!shouldPlay) vunglePub.playAd();
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    shouldPlay = true;
                }
            }
        });

        vunglePub.setEventListener(new EventListener() {
            @Override
            public void onAdEnd(boolean b) {
                AppUtils.addContribution(activity);
                tvContribution.setText(Integer.toString(AppUtils.getContribution(activity)));
            }

            @Override
            public void onAdStart() {

            }

            @Override
            public void onAdUnavailable(String s) {

            }

            @Override
            public void onCachedAdAvailable() {
                if(shouldPlay) {
                    vunglePub.playAd();
                    shouldPlay = false;
                }
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onVideoView(boolean b, int i, int i2) {

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        vunglePub.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        vunglePub.onResume();
    }
}
