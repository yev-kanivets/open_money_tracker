package com.blogspot.e_kanivets.moneytracker.activity.record;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.blogspot.e_kanivets.moneytracker.controller.CurrencyController;
import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.activity.ReportActivity;
import com.blogspot.e_kanivets.moneytracker.activity.base.BaseDrawerActivity;
import com.blogspot.e_kanivets.moneytracker.adapter.RecordAdapter;
import com.blogspot.e_kanivets.moneytracker.controller.PeriodController;
import com.blogspot.e_kanivets.moneytracker.controller.data.AccountController;
import com.blogspot.e_kanivets.moneytracker.controller.data.ExchangeRateController;
import com.blogspot.e_kanivets.moneytracker.controller.PreferenceController;
import com.blogspot.e_kanivets.moneytracker.controller.data.RecordController;
import com.blogspot.e_kanivets.moneytracker.entity.data.Account;
import com.blogspot.e_kanivets.moneytracker.entity.data.Record;
import com.blogspot.e_kanivets.moneytracker.entity.Period;
import com.blogspot.e_kanivets.moneytracker.report.ReportMaker;
import com.blogspot.e_kanivets.moneytracker.report.record.IRecordReport;
import com.blogspot.e_kanivets.moneytracker.ui.AppRateDialog;
import com.blogspot.e_kanivets.moneytracker.ui.PeriodSpinner;
import com.blogspot.e_kanivets.moneytracker.ui.presenter.ShortSummaryPresenter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

public class MainActivity extends BaseDrawerActivity {
    @SuppressWarnings("unused")
    private static final String TAG = "MainActivity";

    private static final int REQUEST_ACTION_RECORD = 1;

    private List<Record> recordList;
    private Period period;

    @Inject
    RecordController recordController;
    @Inject
    ExchangeRateController rateController;
    @Inject
    AccountController accountController;
    @Inject
    CurrencyController currencyController;
    @Inject
    PreferenceController preferenceController;
    @Inject
    PeriodController periodController;

    private ShortSummaryPresenter summaryPresenter;

    @Bind(R.id.list_view)
    ListView listView;
    @Bind(R.id.spinner_period)
    PeriodSpinner spinner;

    TextView tvDefaultAccountTitle;
    TextView tvDefaultAccountSum;
    TextView tvCurrency;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected boolean initData() {
        super.initData();
        getAppComponent().inject(MainActivity.this);

        preferenceController.addLaunchCount();
        summaryPresenter = new ShortSummaryPresenter(MainActivity.this);

        return super.initData();
    }

    @Override
    protected void initViews() {
        super.initViews();

        setTitle(R.string.title_records);

        tvDefaultAccountTitle = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tv_default_account_title);
        tvDefaultAccountSum = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tv_default_account_sum);
        tvCurrency = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tv_currency);

        if (preferenceController.checkRateDialog()) showAppRateDialog();

        registerForContextMenu(listView);

        View summaryView = summaryPresenter.create(true);
        listView.addHeaderView(summaryView);
        summaryView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showReport();
            }
        });

        spinner.setPeriodSelectedListener(new PeriodSpinner.OnPeriodSelectedListener() {
            @Override
            public void onPeriodSelected(Period period) {
                MainActivity.this.period = period;
                periodController.writeLastUsedPeriod(period);
                update();
            }
        });
        spinner.setPeriod(periodController.readLastUsedPeriod());
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu_record, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
            case R.id.edit:
                // Minus one because of list view's header view
                Record record = recordList.get(info.position - 1);
                if (record.isIncome())
                    startAddIncomeActivity(record, AddRecordActivity.Mode.MODE_EDIT);
                else startAddExpenseActivity(record, AddRecordActivity.Mode.MODE_EDIT);
                return true;

            case R.id.delete:
                // Minus one because of list view's header view
                recordController.delete(recordList.get(info.position - 1));
                update();
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

    @OnClick(R.id.btn_add_expense)
    public void addExpense() {
        startAddExpenseActivity(null, AddRecordActivity.Mode.MODE_ADD);
    }

    @OnClick(R.id.btn_add_income)
    public void addIncome() {
        startAddIncomeActivity(null, AddRecordActivity.Mode.MODE_ADD);
    }

    public void showReport() {
        Intent intent = new Intent(MainActivity.this, ReportActivity.class);
        intent.putExtra(ReportActivity.KEY_PERIOD, period);
        intent.putExtra(ReportActivity.KEY_RECORD_LIST, (ArrayList<Record>) recordList);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == AppCompatActivity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_ACTION_RECORD:
                    update();
                    break;

                default:
                    break;
            }
        }
    }

    @Override
    protected void update() {
        recordList = recordController.getRecordsForPeriod(period);
        Collections.reverse(recordList);

        listView.setAdapter(new RecordAdapter(MainActivity.this, recordList));

        String currency = currencyController.readDefaultCurrency();

        ReportMaker reportMaker = new ReportMaker(rateController);
        IRecordReport report = reportMaker.getRecordReport(currency, period, recordList);
        summaryPresenter.update(report, currency, reportMaker.currencyNeeded(currency, recordList));

        fillDefaultAccount();
    }

    private void showAppRateDialog() {
        AppRateDialog dialog = new AppRateDialog(MainActivity.this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void startAddIncomeActivity(Record record, AddRecordActivity.Mode mode) {
        Intent intent = new Intent(MainActivity.this, AddRecordActivity.class);
        intent.putExtra(AddRecordActivity.KEY_RECORD, record);
        intent.putExtra(AddRecordActivity.KEY_MODE, mode);
        intent.putExtra(AddRecordActivity.KEY_TYPE, Record.TYPE_INCOME);
        startActivityForResult(intent, REQUEST_ACTION_RECORD);
    }

    private void startAddExpenseActivity(Record record, AddRecordActivity.Mode mode) {
        Intent intent = new Intent(MainActivity.this, AddRecordActivity.class);
        intent.putExtra(AddRecordActivity.KEY_RECORD, record);
        intent.putExtra(AddRecordActivity.KEY_MODE, mode);
        intent.putExtra(AddRecordActivity.KEY_TYPE, Record.TYPE_EXPENSE);
        startActivityForResult(intent, REQUEST_ACTION_RECORD);
    }

    @SuppressLint("SetTextI18n")
    private void fillDefaultAccount() {
        Account defaultAccount = accountController.readDefaultAccount();
        if (defaultAccount == null) return;

        tvDefaultAccountTitle.setText(defaultAccount.getTitle());
        tvDefaultAccountSum.setText(Double.toString(defaultAccount.getCurSum()));
        tvCurrency.setText(defaultAccount.getCurrency());
    }
}
