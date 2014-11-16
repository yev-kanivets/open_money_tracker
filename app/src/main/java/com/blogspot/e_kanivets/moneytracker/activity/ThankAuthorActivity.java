package com.blogspot.e_kanivets.moneytracker.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.blogspot.e_kanivets.moneytracker.R;

/**
 * Created by fess on 16.11.2014.
 */
public class ThankAuthorActivity extends Activity {

    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_thank_author);

        activity = this;

        Button btnWatchVideoAd = (Button) findViewById(R.id.btn_watch_video_ad);

        btnWatchVideoAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
            }
        });
    }
}
