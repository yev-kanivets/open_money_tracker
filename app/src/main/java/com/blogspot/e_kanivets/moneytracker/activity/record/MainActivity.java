package com.blogspot.e_kanivets.moneytracker.activity.record;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.blogspot.e_kanivets.moneytracker.DbHelper;
import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.activity.ReportActivity;
import com.blogspot.e_kanivets.moneytracker.activity.base.BaseDrawerActivity;
import com.blogspot.e_kanivets.moneytracker.adapter.RecordAdapter;
import com.blogspot.e_kanivets.moneytracker.controller.AccountController;
import com.blogspot.e_kanivets.moneytracker.controller.CategoryController;
import com.blogspot.e_kanivets.moneytracker.controller.ExchangeRateController;
import com.blogspot.e_kanivets.moneytracker.controller.RecordController;
import com.blogspot.e_kanivets.moneytracker.entity.Category;
import com.blogspot.e_kanivets.moneytracker.entity.Record;
import com.blogspot.e_kanivets.moneytracker.model.Period;
import com.blogspot.e_kanivets.moneytracker.repo.AccountRepo;
import com.blogspot.e_kanivets.moneytracker.repo.CategoryRepo;
import com.blogspot.e_kanivets.moneytracker.repo.ExchangeRateRepo;
import com.blogspot.e_kanivets.moneytracker.repo.RecordRepo;
import com.blogspot.e_kanivets.moneytracker.repo.base.IRepo;
import com.blogspot.e_kanivets.moneytracker.report.ReportMaker;
import com.blogspot.e_kanivets.moneytracker.report.base.IReport;
import com.blogspot.e_kanivets.moneytracker.ui.AppRateDialog;
import com.blogspot.e_kanivets.moneytracker.ui.PeriodSpinner;
import com.blogspot.e_kanivets.moneytracker.ui.ShortSummaryPresenter;
import com.blogspot.e_kanivets.moneytracker.util.PrefUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class MainActivity extends BaseDrawerActivity {
    @SuppressWarnings("unused")
    private static final String TAG = "MainActivity";

    private static final int REQUEST_ACTION_RECORD = 1;

    private List<Record> recordList;
    private Period period;

    private RecordController recordController;
    private ExchangeRateController rateController;
    private AccountController accountController;
    private ShortSummaryPresenter summaryPresenter;

    @Bind(R.id.list_view)
    ListView listView;
    @Bind(R.id.spinner_period)
    PeriodSpinner spinner;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected boolean initData() {
        PrefUtils.addLaunchCount();

        DbHelper dbHelper = new DbHelper(MainActivity.this);
        IRepo<Category> categoryRepo = new CategoryRepo(dbHelper);
        CategoryController categoryController = new CategoryController(categoryRepo);
        accountController = new AccountController(new AccountRepo(dbHelper));
        IRepo<Record> recordRepo = new RecordRepo(dbHelper);

        recordController = new RecordController(recordRepo, categoryController, accountController);

        rateController = new ExchangeRateController(new ExchangeRateRepo(dbHelper));

        summaryPresenter = new ShortSummaryPresenter(MainActivity.this);

        return super.initData();
    }

    @Override
    protected void initViews() {
        super.initViews();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (PrefUtils.checkRateDialog()) showAppRateDialog();

        registerForContextMenu(listView);

        View summaryView = summaryPresenter.create();
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
                PrefUtils.writePeriod(period);
                update();
            }
        });
        spinner.setPeriod(PrefUtils.readPeriod());
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

    private void update() {
        recordList = recordController.getRecordsForPeriod(period);
        Collections.reverse(recordList);

        listView.setAdapter(new RecordAdapter(MainActivity.this, recordList));

        String currency = DbHelper.DEFAULT_ACCOUNT_CURRENCY;
        if (accountController.readAll().size() > 0)
            currency = accountController.readAll().get(0).getCurrency();

        ReportMaker reportMaker = new ReportMaker(rateController);
        IReport report = reportMaker.getReport(currency, period, recordList);
        summaryPresenter.update(report);
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
}
