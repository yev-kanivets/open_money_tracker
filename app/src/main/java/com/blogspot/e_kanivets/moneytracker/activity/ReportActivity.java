package com.blogspot.e_kanivets.moneytracker.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.adapter.ReportItemAdapter;
import com.blogspot.e_kanivets.moneytracker.helper.MTHelper;
import com.blogspot.e_kanivets.moneytracker.model.Report;

/**
 * Created by eugene on 11/09/14.
 */
public class ReportActivity extends Activity {

    private Activity activity;

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_report);

        activity = this;

        Button btnThankAuthor;

        btnThankAuthor = (Button) findViewById(R.id.btn_thank_author);
        listView = (ListView) findViewById(R.id.listView);

        /*tvTitle.setText("REPORT (" + MTHelper.getInstance().getFirstDay() + " - "
                + MTHelper.getInstance().getLastDay() + ")");*/

        btnThankAuthor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReportActivity.this, ThankAuthorActivity.class);
                startActivity(intent);
            }
        });

        listView.setAdapter(new ReportItemAdapter(activity,
                new Report(MTHelper.getInstance().getRecords()).getReportList()));

        /* Scroll list to bottom only once at start */
        listView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            private boolean isFirst = true;

            @Override
            public void onGlobalLayout() {
                if(isFirst) {
                    isFirst = false;
                    listView.setSelection(listView.getCount()-1);
                }
            }
        });
    }
}
