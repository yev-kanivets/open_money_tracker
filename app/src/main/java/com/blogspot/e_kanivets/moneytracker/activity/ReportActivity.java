package com.blogspot.e_kanivets.moneytracker.activity;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.ExpandableListView;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.activity.base.BaseActivity;
import com.blogspot.e_kanivets.moneytracker.adapter.ExpandableListReportAdapter;
import com.blogspot.e_kanivets.moneytracker.controller.AccountController;
import com.blogspot.e_kanivets.moneytracker.controller.ExchangeRateController;
import com.blogspot.e_kanivets.moneytracker.DbHelper;
import com.blogspot.e_kanivets.moneytracker.model.Period;
import com.blogspot.e_kanivets.moneytracker.model.Record;
import com.blogspot.e_kanivets.moneytracker.repo.AccountRepo;
import com.blogspot.e_kanivets.moneytracker.repo.ExchangeRateRepo;
import com.blogspot.e_kanivets.moneytracker.report.ReportConverter;
import com.blogspot.e_kanivets.moneytracker.report.ReportMaker;
import com.blogspot.e_kanivets.moneytracker.report.base.IReport;
import com.blogspot.e_kanivets.moneytracker.ui.TotalReportViewCreator;

import java.util.List;

import butterknife.Bind;

public class ReportActivity extends BaseActivity {
    @SuppressWarnings("unused")
    private static final String TAG = "ReportActivity";

    public static final String KEY_PERIOD = "key_period";
    public static final String KEY_RECORD_LIST = "key_record_list";

    @Bind(R.id.exp_list_view)
    ExpandableListView expandableListView;
    private IReport report;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_report;
    }

    @Override
    protected boolean initData() {
        super.initData();

        List<Record> recordList = getIntent().getParcelableArrayListExtra(KEY_RECORD_LIST);
        if (recordList == null) return false;

        Period period = getIntent().getParcelableExtra(KEY_PERIOD);
        if (period == null) return false;

        DbHelper dbHelper = new DbHelper(ReportActivity.this);
        AccountController accountController = new AccountController(new AccountRepo(dbHelper));
        ExchangeRateController rateController = new ExchangeRateController(new ExchangeRateRepo(dbHelper));

        String currency = DbHelper.DEFAULT_ACCOUNT_CURRENCY;
        if (accountController.readAll().size() > 0)
            currency = accountController.readAll().get(0).getCurrency();

        ReportMaker reportMaker = new ReportMaker(rateController);
        report = reportMaker.getReport(currency, period, recordList);

        if (report == null) {
            List<String> ratesNeeded = reportMaker.currencyNeeded(currency, recordList);
            showExchangeRatesNeededDialog(currency, ratesNeeded);
        }

        return true;
    }

    @Override
    protected void initViews() {
        super.initViews();

        if (report == null) return;
        ReportConverter reportConverter = new ReportConverter(report);

        expandableListView.addFooterView(new TotalReportViewCreator(ReportActivity.this, report).create());
        expandableListView.setAdapter(new ExpandableListReportAdapter(ReportActivity.this, reportConverter));
    }

    private void showExchangeRatesNeededDialog(String currency, List<String> ratesNeeded) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ReportActivity.this);
        builder.setTitle(getString(R.string.cant_make_report));

        StringBuilder sb = new StringBuilder(getString(R.string.rates_needed));
        for (String str : ratesNeeded) {
            sb.append("\n").append(str).append(getString(R.string.arrow)).append(currency);
        }

        builder.setMessage(sb.toString());

        builder.setPositiveButton(android.R.string.ok, null);

        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                finish();
            }
        });

        builder.show();
    }
}