package com.blogspot.e_kanivets.moneytracker.activity.record;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.blogspot.e_kanivets.moneytracker.DbHelper;
import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.activity.ExportActivity;
import com.blogspot.e_kanivets.moneytracker.activity.ReportActivity;
import com.blogspot.e_kanivets.moneytracker.activity.account.AccountsActivity;
import com.blogspot.e_kanivets.moneytracker.activity.base.BaseActivity;
import com.blogspot.e_kanivets.moneytracker.activity.exchange_rate.ExchangeRatesActivity;
import com.blogspot.e_kanivets.moneytracker.adapter.RecordAdapter;
import com.blogspot.e_kanivets.moneytracker.controller.AccountController;
import com.blogspot.e_kanivets.moneytracker.controller.CategoryController;
import com.blogspot.e_kanivets.moneytracker.controller.ExchangeRateController;
import com.blogspot.e_kanivets.moneytracker.controller.PeriodController;
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
import com.blogspot.e_kanivets.moneytracker.ui.SummaryRecordsPresenter;
import com.blogspot.e_kanivets.moneytracker.util.PrefUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    @SuppressWarnings("unused")
    private static final String TAG = "MainActivity";

    private static final int REQUEST_ACTION_RECORD = 1;

    private List<Record> recordList;

    private RecordController recordController;
    private PeriodController periodController;
    private ExchangeRateController rateController;
    private AccountController accountController;

    private IReport report;

    @Bind(R.id.drawer_layout)
    DrawerLayout drawer;

    @Bind(R.id.list_view)
    ListView listView;
    @Bind(R.id.spinner_period)
    AppCompatSpinner spinner;
    private SummaryRecordsPresenter summaryPresenter;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected boolean initData() {
        PrefUtils.addLaunchCount();

        periodController = new PeriodController();

        DbHelper dbHelper = new DbHelper(MainActivity.this);
        IRepo<Category> categoryRepo = new CategoryRepo(dbHelper);
        CategoryController categoryController = new CategoryController(categoryRepo);
        accountController = new AccountController(new AccountRepo(dbHelper));
        IRepo<Record> recordRepo = new RecordRepo(dbHelper);

        recordController = new RecordController(recordRepo, categoryController, accountController);

        rateController = new ExchangeRateController(new ExchangeRateRepo(dbHelper));

        summaryPresenter = new SummaryRecordsPresenter(MainActivity.this);

        return super.initData();
    }

    @Override
    protected void initViews() {
        super.initViews();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (PrefUtils.checkRateDialog()) showAppRateDialog();

        registerForContextMenu(listView);

        spinner.setAdapter(new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.array_periods)));
        spinner.setSelection(PrefUtils.readUsedPeriod());
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Calendar calendar = Calendar.getInstance();
                calendar.clear(Calendar.MINUTE);
                calendar.clear(Calendar.SECOND);
                calendar.clear(Calendar.MILLISECOND);

                switch (position) {
                    case 0:
                        periodController.setPeriod(Period.dayPeriod());
                        break;

                    case 1:
                        periodController.setPeriod(Period.weekPeriod());
                        break;

                    case 2:
                        periodController.setPeriod(Period.monthPeriod());
                        break;

                    case 3:
                        periodController.setPeriod(Period.yearPeriod());
                        break;

                    default:
                        break;
                }

                PrefUtils.writeUsedPeriod(position);

                update();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        View summaryView = summaryPresenter.create();
        listView.addHeaderView(summaryView);
        summaryView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showReport();
            }
        });

        update();
    }

    @Override
    public void onBackPressed() {
        if (drawer != null && drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
        else super.onBackPressed();
    }

    @Override
    protected Toolbar initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        return toolbar;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_accounts:
                startActivity(new Intent(MainActivity.this, AccountsActivity.class));
                break;

            case R.id.nav_rates:
                startActivity(new Intent(MainActivity.this, ExchangeRatesActivity.class));
                break;

            case R.id.nav_export:
                startActivity(new Intent(MainActivity.this, ExportActivity.class));
                break;

            default:
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return false;
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
                recordController.delete(recordList.get(info.position));
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
        intent.putExtra(ReportActivity.KEY_PERIOD, periodController.getPeriod());
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
        recordList = recordController.getRecordsForPeriod(periodController.getPeriod());
        Collections.reverse(recordList);

        listView.setAdapter(new RecordAdapter(MainActivity.this, recordList));

        String currency = DbHelper.DEFAULT_ACCOUNT_CURRENCY;
        if (accountController.readAll().size() > 0)
            currency = accountController.readAll().get(0).getCurrency();

        ReportMaker reportMaker = new ReportMaker(rateController);
        report = reportMaker.getReport(currency, periodController.getPeriod(), recordList);
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
