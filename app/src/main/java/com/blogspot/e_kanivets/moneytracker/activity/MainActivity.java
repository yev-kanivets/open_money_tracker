package com.blogspot.e_kanivets.moneytracker.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.adapter.RecordAdapter;
import com.blogspot.e_kanivets.moneytracker.helper.MTHelper;
import com.blogspot.e_kanivets.moneytracker.helper.PeriodHelper;
import com.blogspot.e_kanivets.moneytracker.model.Record;
import com.blogspot.e_kanivets.moneytracker.ui.AddExpenseDialog;
import com.blogspot.e_kanivets.moneytracker.ui.AddIncomeDialog;
import com.blogspot.e_kanivets.moneytracker.ui.AppRateDialog;
import com.blogspot.e_kanivets.moneytracker.ui.ChangeDateDialog;
import com.blogspot.e_kanivets.moneytracker.util.AppUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;

public class MainActivity extends Activity implements Observer{

    private Activity activity;

    private ListView listView;

    private TextView tvFromDate;
    private TextView tvToDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_main);

        activity = this;

        /* Increment launch count */
        AppUtils.addLaunchCount(activity);

        Button btnAddIncome;
        Button btnAddExpense;
        Button btnReport;
        Button btnSelectPeriod;

        //Link views
        btnAddIncome = (Button) findViewById(R.id.btn_add_income);
        btnAddExpense = (Button) findViewById(R.id.btn_add_expense);
        btnReport = (Button) findViewById(R.id.btn_report);
        btnSelectPeriod = (Button) findViewById(R.id.btn_select_period);

        tvFromDate = (TextView) findViewById(R.id.tv_from_date);
        tvToDate = (TextView) findViewById(R.id.tv_to_date);

        listView = (ListView) findViewById(R.id.listView);

        //Set dates of current week
        tvFromDate.setText(MTHelper.getInstance().getFirstDay());
        tvToDate.setText(MTHelper.getInstance().getLastDay());

        //Set listeners
        btnAddIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddIncomeDialog dialog = new AddIncomeDialog(activity, null, AddIncomeDialog.Mode.MODE_ADD);
                dialog.show();
            }
        });

        btnAddExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddExpenseDialog dialog = new AddExpenseDialog(activity, null, AddExpenseDialog.Mode.MODE_ADD);
                dialog.show();
            }
        });
        btnSelectPeriod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSelectPopupMenu(view);
            }
        });

        tvFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangeDateDialog dialog = new ChangeDateDialog(activity,
                        MTHelper.getInstance().getPeriod().getFirst(), new ChangeDateDialog.OnDateChangedListener() {
                    @Override
                    public void OnDataChanged(Date date) {
                        MTHelper.getInstance().getPeriod().setFirst(date);
                        MTHelper.getInstance().update();

                        tvFromDate.setText(MTHelper.getInstance().getFirstDay());
                    }
                });
                dialog.show();
            }
        });

        tvToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangeDateDialog dialog = new ChangeDateDialog(activity,
                        MTHelper.getInstance().getPeriod().getLast(), new ChangeDateDialog.OnDateChangedListener() {
                    @Override
                    public void OnDataChanged(Date date) {
                        MTHelper.getInstance().getPeriod().setLast(date);
                        MTHelper.getInstance().update();

                        tvToDate.setText(MTHelper.getInstance().getLastDay());
                    }
                });
                dialog.show();
            }
        });

        btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ReportActivity.class);
                startActivity(intent);
            }
        });

        listView.setAdapter(new RecordAdapter(activity, MTHelper.getInstance().getRecords()));
        ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
        registerForContextMenu(listView);

        /* Scroll list to bottom only once at start */
        listView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            private boolean isFirst = true;

            @Override
            public void onGlobalLayout() {
                if(isFirst) {
                    isFirst = false;
                    listView.setSelection(listView.getCount()-1);
                    if(AppUtils.checkRateDialog(activity)) {
                        AppRateDialog dialog = new AppRateDialog(activity);
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.show();
                    }
                }
            }
        });

        //Subscribe to helper
        MTHelper.getInstance().addObserver(this);
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
                Record record  = MTHelper.getInstance().getRecords().get(info.position);
                if(record.isIncome()) {
                    AddIncomeDialog dialog = new AddIncomeDialog(activity, record, AddIncomeDialog.Mode.MODE_EDIT);
                    dialog.show();
                } else {
                    AddExpenseDialog dialog = new AddExpenseDialog(activity, record, AddExpenseDialog.Mode.MODE_EDIT);
                    dialog.show();
                }
                return true;
            case R.id.delete:
                MTHelper.getInstance().deleteRecordById(MTHelper.getInstance().getRecords().
                        get(info.position).getId());
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void update(Observable observable, Object data) {
        ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
    }

    private void showSelectPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(activity, view);
        popupMenu.getMenuInflater().inflate(R.menu.menu_popup, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Calendar calendar = Calendar.getInstance();
                calendar.clear(Calendar.MINUTE);
                calendar.clear(Calendar.SECOND);
                calendar.clear(Calendar.MILLISECOND);

                switch (menuItem.getItemId()) {
                    case R.id.period_day:
                        MTHelper.getInstance().setPeriod(PeriodHelper.getInstance().getDayPeriod());
                        break;
                    case R.id.period_week:
                        MTHelper.getInstance().setPeriod(PeriodHelper.getInstance().getWeekPeriod());
                        break;
                    case R.id.period_month:
                        MTHelper.getInstance().setPeriod(PeriodHelper.getInstance().getMonthPeriod());
                        break;
                    case R.id.period_year:
                        MTHelper.getInstance().setPeriod(PeriodHelper.getInstance().getYearPeriod());
                        break;
                    default:
                        break;
                }

                MTHelper.getInstance().update();

                tvFromDate.setText(MTHelper.getInstance().getFirstDay());
                tvToDate.setText(MTHelper.getInstance().getLastDay());

                return false;
            }
        });

        popupMenu.show();
    }
}
