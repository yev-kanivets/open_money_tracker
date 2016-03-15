package com.blogspot.e_kanivets.moneytracker.activity;

import android.support.v7.widget.AppCompatSpinner;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.blogspot.e_kanivets.moneytracker.DbHelper;
import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.activity.base.BaseBackActivity;
import com.blogspot.e_kanivets.moneytracker.controller.ExchangeRateController;
import com.blogspot.e_kanivets.moneytracker.entity.ExchangeRate;
import com.blogspot.e_kanivets.moneytracker.repo.ExchangeRateRepo;
import com.blogspot.e_kanivets.moneytracker.util.CurrencyProvider;

import java.util.ArrayList;

import butterknife.Bind;

public class AddExchangeRateActivity extends BaseBackActivity {
    @SuppressWarnings("unused")
    private static final String TAG = "AddExchangeRateActivity";

    @Bind(R.id.spinner_from_currency)
    AppCompatSpinner spinnerFromCurrency;
    @Bind(R.id.spinner_to_currency)
    AppCompatSpinner spinnerToCurrency;
    @Bind(R.id.et_amount)
    EditText etAmount;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_add_exchange_rate;
    }

    @Override
    protected void initViews() {
        super.initViews();

        spinnerFromCurrency.setAdapter(new ArrayAdapter<>(AddExchangeRateActivity.this,
                android.R.layout.simple_list_item_1,
                new ArrayList<>(CurrencyProvider.getAllCurrencies())));

        spinnerToCurrency.setAdapter(new ArrayAdapter<>(AddExchangeRateActivity.this,
                android.R.layout.simple_list_item_1,
                new ArrayList<>(CurrencyProvider.getAllCurrencies())));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_exchange_rate, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                if (addExchangeRate()) {
                    setResult(RESULT_OK);
                    finish();
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean addExchangeRate() {
        String fromCurrency = (String) spinnerFromCurrency.getSelectedItem();
        String toCurrency = (String) spinnerToCurrency.getSelectedItem();
        double amount = -1;

        try {
            amount = Double.parseDouble(etAmount.getText().toString().trim());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (amount == -1) return false;

        ExchangeRate exchangeRate = new ExchangeRate(System.currentTimeMillis(),
                fromCurrency, toCurrency, amount);

        ExchangeRate createdRate = new ExchangeRateController(new ExchangeRateRepo(
                new DbHelper(AddExchangeRateActivity.this))).create(exchangeRate);

        return createdRate != null;
    }
}