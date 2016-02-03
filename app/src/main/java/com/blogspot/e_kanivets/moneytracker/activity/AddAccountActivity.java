package com.blogspot.e_kanivets.moneytracker.activity;

import android.support.v7.widget.AppCompatSpinner;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SimpleAdapter;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.activity.base.BaseActivity;
import com.blogspot.e_kanivets.moneytracker.controller.AccountController;
import com.blogspot.e_kanivets.moneytracker.helper.DbHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Currency;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

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
                android.R.layout.simple_list_item_1, new ArrayList<>(getAllCurrencies())));
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

    public static List<String> getAllCurrencies() {
        Set<Currency> toret = new HashSet<>();
        Locale[] locs = Locale.getAvailableLocales();

        for (Locale loc : locs) {
            try {
                toret.add(Currency.getInstance(loc));
            } catch (Exception exc) {
                // Locale not found
            }
        }

        List<String> currencyList = new ArrayList<>();
        for (Currency currency : toret) {
            currencyList.add(currency.getCurrencyCode());
        }

        Collections.sort(currencyList);

        return currencyList;
    }
}