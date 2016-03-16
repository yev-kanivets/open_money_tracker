package com.blogspot.e_kanivets.moneytracker.ui;

import android.content.Context;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.blogspot.e_kanivets.moneytracker.DbHelper;
import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.controller.AccountController;
import com.blogspot.e_kanivets.moneytracker.controller.ExchangeRateController;
import com.blogspot.e_kanivets.moneytracker.repo.AccountRepo;
import com.blogspot.e_kanivets.moneytracker.repo.ExchangeRateRepo;
import com.blogspot.e_kanivets.moneytracker.report.ReportMaker;
import com.blogspot.e_kanivets.moneytracker.report.base.IAccountsReport;
import com.blogspot.e_kanivets.moneytracker.util.CurrencyProvider;

import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Util class to create and manage summary header view for .
 * Created on 2/26/16.
 *
 * @author Evgenii Kanivets
 */
public class AccountsSummaryPresenter {
    private Context context;
    private final LayoutInflater layoutInflater;

    private AccountController accountController;

    private int red;
    private int green;
    private View view;
    private final ReportMaker reportMaker;

    @SuppressWarnings("deprecation")
    public AccountsSummaryPresenter(Context context) {
        this.context = context;

        layoutInflater = LayoutInflater.from(context);
        red = context.getResources().getColor(R.color.red);
        green = context.getResources().getColor(R.color.green);

        DbHelper dbHelper = new DbHelper(context);
        accountController = new AccountController(new AccountRepo(dbHelper));
        ExchangeRateController rateController = new ExchangeRateController(new ExchangeRateRepo(dbHelper));
        reportMaker = new ReportMaker(rateController);
    }

    public View create() {
        view = layoutInflater.inflate(R.layout.view_summary_accounts, null);

        final ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        List<String> currencyList = CurrencyProvider.getAllCurrencies();

        viewHolder.spinnerCurrency.setAdapter(new ArrayAdapter<>(context,
                android.R.layout.simple_list_item_1, currencyList));

        String currency = DbHelper.DEFAULT_ACCOUNT_CURRENCY;
        if (accountController.readAll().size() > 0)
            currency = accountController.readAll().get(0).getCurrency();

        for (int i = 0; i < currencyList.size(); i++) {
            String item = currencyList.get(i);

            if (item.equals(currency)) {
                viewHolder.spinnerCurrency.setSelection(i);
                break;
            }
        }

        viewHolder.spinnerCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                update();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }

    public void update() {
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        String currency = (String) viewHolder.spinnerCurrency.getSelectedItem();
        IAccountsReport report = reportMaker.getAccountsReport(currency, accountController.readAll());

        if (report == null) {
            viewHolder.tvTotal.setTextColor(red);
            viewHolder.tvTotal.setText(R.string.error_exchange_rates);
        } else {
            viewHolder.tvTotal.setTextColor(report.getTotal() >= 0 ? green : red);
            viewHolder.tvTotal.setText(format(report.getTotal(), report.getCurrency()));
        }
    }

    private String format(double amount, String currency) {
        return (amount >= 0 ? "+ " : "- ") + String.format(Locale.getDefault(), "%.0f", Math.abs(amount))
                + " " + currency;
    }

    public static class ViewHolder {
        @Bind(R.id.spinner_currency)
        AppCompatSpinner spinnerCurrency;
        @Bind(R.id.tv_total)
        TextView tvTotal;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
