package com.blogspot.e_kanivets.moneytracker.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.FeatureInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.adapter.RecordAdapter;
import com.blogspot.e_kanivets.moneytracker.helper.DBHelper;
import com.blogspot.e_kanivets.moneytracker.helper.MTHelper;
import com.blogspot.e_kanivets.moneytracker.model.Record;
import com.blogspot.e_kanivets.moneytracker.ui.AddExpenseDialog;
import com.blogspot.e_kanivets.moneytracker.ui.AddIncomeDialog;
import com.blogspot.e_kanivets.moneytracker.ui.ChangeDateDialog;
import com.blogspot.e_kanivets.moneytracker.util.Constants;
import com.blogspot.e_kanivets.moneytracker.util.MTApp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.Observer;


public class MainActivity extends Activity implements Observer{

    private Activity activity;

    private ListView listView;

    private Button btnAddIncome;
    private Button btnAddExpense;
    private Button btnReport;
    private TextView tvFromDate;
    private TextView tvToDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_main);

        activity = this;

        //Link views
        btnAddIncome = (Button) findViewById(R.id.btn_add_income);
        btnAddExpense = (Button) findViewById(R.id.btn_add_expense);
        btnReport = (Button) findViewById(R.id.btn_report);

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
                /*Log.d(Constants.TAG, "pos = " + info.position + " id = " + MTHelper.getInstance().getRecords().
                        get(info.position).getId());*/
                MTHelper.getInstance().deleteRecordById(MTHelper.getInstance().getRecords().
                        get(info.position).getId());
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void update(Observable observable, Object data) {
        ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
    }
}
