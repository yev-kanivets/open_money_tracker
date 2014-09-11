package com.blogspot.e_kanivets.moneytracker.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.helper.MTHelper;

/**
 * Created by eugene on 11/09/14.
 */
public class ReportActivity extends Activity {

    private TextView tvTitle;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        tvTitle = (TextView) findViewById(R.id.tv_title);
        listView = (ListView) findViewById(R.id.listView);

        /*tvTitle.setText("REPORT (" + MTHelper.getInstance().getFirstDay() + " - "
                + MTHelper.getInstance().getLastDay() + ")");*/


    }
}
