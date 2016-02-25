package com.blogspot.e_kanivets.moneytracker.activity;

import android.support.v7.widget.AppCompatSpinner;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.activity.base.BaseActivity;
import com.blogspot.e_kanivets.moneytracker.DbHelper;
import com.blogspot.e_kanivets.moneytracker.controller.AccountController;
import com.blogspot.e_kanivets.moneytracker.model.Account;
import com.blogspot.e_kanivets.moneytracker.repo.AccountRepo;
import com.blogspot.e_kanivets.moneytracker.util.CurrencyProvider;

import java.util.ArrayList;

import butterknife.Bind;

public class AddAccountActivity extends BaseActivity {
    @SuppressWarnings("unused")
    private static final String TAG = "AddAccountActivity";

    @Bind(R.id.et_title)
    EditText etTitle;
    @Bind(R.id.et_init_sum)
    EditText etInitSum;
    @Bind(R.id.spinner)
    AppCompatSpinner spinner;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_add_account;
    }

    @Override
    protected void initViews() {
        super.initViews();

        spinner.setAdapter(new ArrayAdapter<>(AddAccountActivity.this,
                android.R.layout.simple_list_item_1,
                new ArrayList<>(CurrencyProvider.getAllCurrencies())));
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
        String currency = (String) spinner.getSelectedItem();

        Account account = new Account(title, initSum, currency);

        new AccountController(new AccountRepo(new DbHelper(AddAccountActivity.this))).create(account);
    }
}