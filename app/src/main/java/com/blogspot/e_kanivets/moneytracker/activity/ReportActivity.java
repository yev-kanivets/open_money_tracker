package com.blogspot.e_kanivets.moneytracker.activity;

import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.activity.base.BaseBackActivity;
import com.blogspot.e_kanivets.moneytracker.adapter.ExpandableListReportAdapter;
import com.blogspot.e_kanivets.moneytracker.controller.CurrencyController;
import com.blogspot.e_kanivets.moneytracker.controller.data.ExchangeRateController;
import com.blogspot.e_kanivets.moneytracker.entity.Period;
import com.blogspot.e_kanivets.moneytracker.entity.data.Record;
import com.blogspot.e_kanivets.moneytracker.report.record.RecordReportConverter;
import com.blogspot.e_kanivets.moneytracker.report.ReportMaker;
import com.blogspot.e_kanivets.moneytracker.report.record.IRecordReport;
import com.blogspot.e_kanivets.moneytracker.ui.presenter.ShortSummaryPresenter;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;

public class ReportActivity extends BaseBackActivity {
    @SuppressWarnings("unused")
    private static final String TAG = "ReportActivity";

    public static final String KEY_PERIOD = "key_period";
    public static final String KEY_RECORD_LIST = "key_record_list";

    @Inject
    ExchangeRateController rateController;
    @Inject
    CurrencyController currencyController;

    private List<Record> recordList;
    private Period period;

    private ShortSummaryPresenter shortSummaryPresenter;

    @Bind(R.id.spinner_currency)
    AppCompatSpinner spinnerCurrency;
    @Bind(R.id.exp_list_view)
    ExpandableListView expandableListView;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_report;
    }

    @Override
    protected boolean initData() {
        super.initData();
        getAppComponent().inject(ReportActivity.this);

        recordList = getIntent().getParcelableArrayListExtra(KEY_RECORD_LIST);
        if (recordList == null) return false;

        period = getIntent().getParcelableExtra(KEY_PERIOD);
        if (period == null) return false;

        return true;
    }

    @Override
    protected void initViews() {
        super.initViews();

        initSpinnerCurrency();

        shortSummaryPresenter = new ShortSummaryPresenter(ReportActivity.this);
        expandableListView.addHeaderView(shortSummaryPresenter.create(false));
    }

    private void update(String currency) {
        ReportMaker reportMaker = new ReportMaker(rateController);
        IRecordReport report = reportMaker.getRecordReport(currency, period, recordList);

        ExpandableListReportAdapter adapter = null;

        if (report != null) {
            RecordReportConverter recordReportConverter = new RecordReportConverter(report);
            adapter = new ExpandableListReportAdapter(ReportActivity.this, recordReportConverter);
        }

        expandableListView.setAdapter(adapter);
        shortSummaryPresenter.update(report, currency, reportMaker.currencyNeeded(currency, recordList));
    }

    private void initSpinnerCurrency() {
        List<String> currencyList = currencyController.readAll();

        spinnerCurrency.setAdapter(new ArrayAdapter<>(ReportActivity.this,
                R.layout.view_spinner_item, currencyList));
        spinnerCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                update((String) spinnerCurrency.getSelectedItem());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        String currency = currencyController.readDefaultCurrency();

        for (int i = 0; i < currencyList.size(); i++) {
            if (currency.equals(currencyList.get(i))) {
                spinnerCurrency.setSelection(i);
                break;
            }
        }
    }
}