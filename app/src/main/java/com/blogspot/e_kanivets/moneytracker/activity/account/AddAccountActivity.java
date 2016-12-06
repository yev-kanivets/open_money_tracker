package com.blogspot.e_kanivets.moneytracker.activity.account;

import android.support.v7.widget.AppCompatSpinner;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.activity.base.BaseBackActivity;
import com.blogspot.e_kanivets.moneytracker.controller.data.AccountController;
import com.blogspot.e_kanivets.moneytracker.controller.CurrencyController;
import com.blogspot.e_kanivets.moneytracker.entity.data.Account;
import com.blogspot.e_kanivets.moneytracker.util.validator.AccountValidator;
import com.blogspot.e_kanivets.moneytracker.util.validator.IValidator;

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

    private IValidator<Account> accountValidator;

    @Bind(R.id.content)
    View contentView;
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

        accountValidator = new AccountValidator(AddAccountActivity.this, contentView);
        spinner.setAdapter(new ArrayAdapter<>(AddAccountActivity.this,
                R.layout.view_spinner_item,
                new ArrayList<>(currencyController.readAll())));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_account, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                if (addAccount()) {
                    setResult(RESULT_OK);
                    finish();
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressWarnings("SimplifiableIfStatement")
    private boolean addAccount() {
        Account account = accountValidator.validate();
        if (account == null) return false;
        else return accountController.create(account) != null;
    }
}
