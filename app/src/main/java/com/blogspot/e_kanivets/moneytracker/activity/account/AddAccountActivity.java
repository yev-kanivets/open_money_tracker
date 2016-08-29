package com.blogspot.e_kanivets.moneytracker.activity.account;

import android.support.v7.widget.AppCompatSpinner;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.activity.base.BaseBackActivity;
import com.blogspot.e_kanivets.moneytracker.controller.data.AccountController;
import com.blogspot.e_kanivets.moneytracker.controller.CurrencyController;
import com.blogspot.e_kanivets.moneytracker.entity.data.Account;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.Bind;

public class AddAccountActivity extends BaseBackActivity {
    @SuppressWarnings("unused")
    private static final String TAG = "AddAccountActivity";

    @Inject
    AccountController accountController;
    @Inject
    CurrencyController currencyController;

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
    protected boolean initData() {
        boolean result = super.initData();
        getAppComponent().inject(AddAccountActivity.this);
        return result;
    }

    @Override
    protected void initViews() {
        super.initViews();

        spinner.setAdapter(new ArrayAdapter<>(AddAccountActivity.this,
                R.layout.view_spinner_item,
                new ArrayList<>(currencyController.readAll())));
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
                if (addAccount()) {
                    setResult(RESULT_OK);
                    finish();
                } else showToast(R.string.wrong_number_text);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean addAccount() {
        String title = etTitle.getText().toString().trim();

        double initSum;
        try {
            initSum = Double.parseDouble(etInitSum.getText().toString().trim());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return false;
        }

        String currency = (String) spinner.getSelectedItem();

        Account account = new Account(title, initSum, currency);
        return accountController.create(account) != null;
    }
}
