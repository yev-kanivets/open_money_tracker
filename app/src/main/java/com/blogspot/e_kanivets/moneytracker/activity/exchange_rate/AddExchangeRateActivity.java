package com.blogspot.e_kanivets.moneytracker.activity.exchange_rate;

import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatSpinner;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.activity.base.BaseBackActivity;
import com.blogspot.e_kanivets.moneytracker.controller.CurrencyController;
import com.blogspot.e_kanivets.moneytracker.controller.FormatController;
import com.blogspot.e_kanivets.moneytracker.controller.data.ExchangeRateController;
import com.blogspot.e_kanivets.moneytracker.entity.ExchangeRatePair;
import com.blogspot.e_kanivets.moneytracker.util.validator.ExchangeRatePairValidator;
import com.blogspot.e_kanivets.moneytracker.util.validator.IValidator;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;

public class AddExchangeRateActivity extends BaseBackActivity {
    @SuppressWarnings("unused")
    private static final String TAG = "AddExchangeRateActivity";

    public static final String KEY_EXCHANGE_RATE = "key_exchange_rate";

    @Inject
    ExchangeRateController exchangeRateController;
    @Inject
    CurrencyController currencyController;
    @Inject
    FormatController formatController;

    private IValidator<ExchangeRatePair> exchangeRatePairValidator;

    // This field passed from Intent and may be used for presetting from/to spinner values
    @Nullable
    private ExchangeRatePair exchangeRatePair;

    @Bind(R.id.content)
    View contentView;
    @Bind(R.id.spinner_from_currency)
    AppCompatSpinner spinnerFromCurrency;
    @Bind(R.id.spinner_to_currency)
    AppCompatSpinner spinnerToCurrency;
    @Bind(R.id.et_buy)
    EditText etBuy;
    @Bind(R.id.et_sell)
    EditText etSell;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_add_exchange_rate;
    }

    @Override
    protected boolean initData() {
        boolean result = super.initData();
        getAppComponent().inject(AddExchangeRateActivity.this);

        exchangeRatePair = getIntent().getParcelableExtra(KEY_EXCHANGE_RATE);

        return result;
    }

    @Override
    protected void initViews() {
        super.initViews();

        exchangeRatePairValidator = new ExchangeRatePairValidator(AddExchangeRateActivity.this, contentView);
        List<String> currencyList = currencyController.readAll();

        if (currencyList.size() == 0) {
            currencyList.add(getString(R.string.none));
            spinnerFromCurrency.setEnabled(false);
            spinnerToCurrency.setEnabled(false);
        }

        spinnerFromCurrency.setAdapter(new ArrayAdapter<>(AddExchangeRateActivity.this,
                R.layout.view_spinner_item,
                new ArrayList<>(currencyList)));

        spinnerToCurrency.setAdapter(new ArrayAdapter<>(AddExchangeRateActivity.this,
                R.layout.view_spinner_item,
                new ArrayList<>(currencyList)));

        // Set selections from passed ExchangeRate
        if (exchangeRatePair != null) {
            for (int i = 0; i < currencyList.size(); i++) {
                if (currencyList.get(i).equals(exchangeRatePair.getFromCurrency())) {
                    spinnerFromCurrency.setSelection(i);
                }
                if (currencyList.get(i).equals(exchangeRatePair.getToCurrency())) {
                    spinnerToCurrency.setSelection(i);
                }
            }

            etBuy.setText(formatController.formatPrecisionNone(exchangeRatePair.getAmountBuy()));
            etSell.setText(formatController.formatPrecisionNone(exchangeRatePair.getAmountSell()));
        }
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

    @SuppressWarnings("SimplifiableIfStatement")
    private boolean addExchangeRate() {
        ExchangeRatePair pair = exchangeRatePairValidator.validate();
        if (pair == null) return false;
        else return exchangeRateController.createExchangeRatePair(pair) != null;
    }
}
