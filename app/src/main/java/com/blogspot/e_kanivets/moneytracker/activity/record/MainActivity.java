package com.blogspot.e_kanivets.moneytracker.activity.record;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.blogspot.e_kanivets.moneytracker.controller.CurrencyController;
import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.activity.ReportActivity;
import com.blogspot.e_kanivets.moneytracker.activity.base.BaseDrawerActivity;
import com.blogspot.e_kanivets.moneytracker.adapter.RecordAdapter;
import com.blogspot.e_kanivets.moneytracker.controller.FormatController;
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
import com.blogspot.e_kanivets.moneytracker.util.AnswersProxy;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnItemClick;

public class MainActivity extends BaseDrawerActivity {
    @SuppressWarnings("unused")
    private static final String TAG = "MainActivity";

    private static final int REQUEST_ACTION_RECORD = 6;

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
    @Inject
    FormatController formatController;

    private ShortSummaryPresenter summaryPresenter;

    @BindView(R.id.list_view)
    ListView listView;
    @BindView(R.id.spinner_period)
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

    @OnItemClick(R.id.list_view)
    public void editRecord(int position) {
        AnswersProxy.get().logButton("Edit Record");
        // Minus one because of list view's header view
        Record record = recordList.get(position - 1);
        startAddRecordActivity(record, AddRecordActivity.Mode.MODE_EDIT, record.getType());
    }

    @OnClick(R.id.btn_add_expense)
    public void addExpense() {
        AnswersProxy.get().logButton("Add Expense");
        startAddRecordActivity(null, AddRecordActivity.Mode.MODE_ADD, Record.TYPE_EXPENSE);
    }

    @OnClick(R.id.btn_add_income)
    public void addIncome() {
        AnswersProxy.get().logButton("Add Income");
        startAddRecordActivity(null, AddRecordActivity.Mode.MODE_ADD, Record.TYPE_INCOME);
    }

    public void showReport() {
        AnswersProxy.get().logButton("Show Report");
        Intent intent = new Intent(MainActivity.this, ReportActivity.class);
        intent.putExtra(ReportActivity.KEY_PERIOD, period);
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

                case REQUEST_BACKUP:
                    getAppComponent().inject(MainActivity.this);
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
        AnswersProxy.get().logEvent("Show App Rate Dialog");
        AppRateDialog dialog = new AppRateDialog(MainActivity.this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void startAddRecordActivity(Record record, AddRecordActivity.Mode mode, int type) {
        Intent intent = new Intent(MainActivity.this, AddRecordActivity.class);
        intent.putExtra(AddRecordActivity.KEY_RECORD, record);
        intent.putExtra(AddRecordActivity.KEY_MODE, mode);
        intent.putExtra(AddRecordActivity.KEY_TYPE, type);
        startActivityForResult(intent, REQUEST_ACTION_RECORD);
    }

    @SuppressLint("SetTextI18n")
    private void fillDefaultAccount() {
        Account defaultAccount = accountController.readDefaultAccount();
        if (defaultAccount == null) return;

        tvDefaultAccountTitle.setText(defaultAccount.getTitle());
        tvDefaultAccountSum.setText(formatController.formatAmount(defaultAccount.getFullSum()));
        tvCurrency.setText(defaultAccount.getCurrency());
    }
}
