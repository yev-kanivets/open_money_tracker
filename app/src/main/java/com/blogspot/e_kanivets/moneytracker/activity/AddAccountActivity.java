package com.blogspot.e_kanivets.moneytracker.activity;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.activity.base.BaseActivity;
import com.blogspot.e_kanivets.moneytracker.controller.AccountController;
import com.blogspot.e_kanivets.moneytracker.helper.DbHelper;

import butterknife.Bind;

public class AddAccountActivity extends BaseActivity {
    @SuppressWarnings("unused")
    private static final String TAG = "AddAccountActivity";

    @Bind(R.id.et_title)
    EditText etTitle;
    @Bind(R.id.et_init_sum)
    EditText etInitSum;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_add_account;
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
                addAccount();

                setResult(RESULT_OK);
                finish();
                return true;

            case R.id.action_close:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void addAccount() {
        String title = etTitle.getText().toString().trim();
        int initSum = Integer.parseInt(etInitSum.getText().toString().trim());

        new AccountController(new DbHelper(AddAccountActivity.this)).addAccount(title, initSum);
    }
}