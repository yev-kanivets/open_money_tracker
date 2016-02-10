package com.blogspot.e_kanivets.moneytracker.activity;

import android.support.v7.widget.AppCompatSpinner;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.activity.base.BaseActivity;
import com.blogspot.e_kanivets.moneytracker.controller.AccountController;
import com.blogspot.e_kanivets.moneytracker.helper.DbHelper;
import com.blogspot.e_kanivets.moneytracker.model.Account;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class TransferActivity extends BaseActivity {
    @SuppressWarnings("unused")
    private static final String TAG = "TransferActivity";

    private AccountController accountController;

    @Bind(R.id.spinner_from)
    AppCompatSpinner spinnerFrom;
    @Bind(R.id.spinner_to)
    AppCompatSpinner spinnerTo;
    @Bind(R.id.et_from_amount)
    EditText etFromAmount;
    @Bind(R.id.et_to_amount)
    EditText etToAmount;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_transfer;
    }

    @Override
    protected boolean initData() {
        accountController = new AccountController(new DbHelper(TransferActivity.this));

        return super.initData();
    }

    @Override
    protected void initViews() {
        super.initViews();

        List<Account> accountList = accountController.getAccounts();

        List<String> accounts = new ArrayList<>();
        for (Account account : accountList) {
            accounts.add(account.getTitle());
        }

        spinnerFrom.setAdapter(new ArrayAdapter<>(TransferActivity.this,
                android.R.layout.simple_list_item_1, accounts));

        spinnerTo.setAdapter(new ArrayAdapter<>(TransferActivity.this,
                android.R.layout.simple_list_item_1, accounts));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_transfer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                doTransfer();

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

    private void doTransfer() {

    }
}