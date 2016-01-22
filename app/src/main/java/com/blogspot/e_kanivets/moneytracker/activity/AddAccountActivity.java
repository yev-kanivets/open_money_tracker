package com.blogspot.e_kanivets.moneytracker.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.helper.MtHelper;

public class AddAccountActivity extends AppCompatActivity {
    @SuppressWarnings("unused")
    private static final String TAG = "AddAccountActivity";

    private EditText etTitle;
    private EditText etInitSum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);

        initViews();
        initActionBar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_record, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                String title = etTitle.getText().toString();
                int initSum = Integer.parseInt(etInitSum.getText().toString());

                MtHelper.getInstance().addAccount(title, initSum);

                finish();
                return true;

            case R.id.action_close:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initViews() {
        etTitle = (EditText) findViewById(R.id.et_title);
        etInitSum = (EditText) findViewById(R.id.et_init_sum);
    }

    private void initActionBar() {
        if (getSupportActionBar() != null) getSupportActionBar().setCustomView(null);
    }
}