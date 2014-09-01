package com.blogspot.e_kanivets.moneytracker.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.adapter.RecordAdapter;
import com.blogspot.e_kanivets.moneytracker.helper.DBHelper;
import com.blogspot.e_kanivets.moneytracker.model.Record;
import com.blogspot.e_kanivets.moneytracker.ui.AddExpenseDialog;
import com.blogspot.e_kanivets.moneytracker.ui.AddIncomeDialog;
import com.blogspot.e_kanivets.moneytracker.util.Constants;
import com.blogspot.e_kanivets.moneytracker.util.MTApp;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    private Activity activity;

    private DBHelper dbHelper;

    private ListView listView;

    private Button btnAddIncome;
    private Button btnAddExpense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity = this;
        dbHelper = MTApp.get().getDbHelper();

        //Link views
        btnAddIncome = (Button) findViewById(R.id.b_add_income);
        btnAddExpense = (Button) findViewById(R.id.b_add_expense);

        listView = (ListView) findViewById(R.id.listView);

        //Set listeners
        btnAddIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddIncomeDialog dialog = new AddIncomeDialog(activity);

                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        Log.d(Constants.TAG, "onDismiss");
                        retrieveDataFromDB();
                    }
                });

                dialog.show();
            }
        });

        btnAddExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddExpenseDialog dialog = new AddExpenseDialog(activity);

                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        Log.d(Constants.TAG, "onDismiss");
                        retrieveDataFromDB();
                    }
                });

                dialog.show();
            }
        });

        retrieveDataFromDB();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void retrieveDataFromDB() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(Constants.TABLE_RECORDS, null, null, null, null, null, null);

        final List<Record> records = new ArrayList<Record>();

        if(cursor.moveToFirst()) {
            //Get indexes of columns
            int idColIndex = cursor.getColumnIndex("id");
            int titleColIndex = cursor.getColumnIndex("title");
            int categoryColIndex = cursor.getColumnIndex("category_id");
            int priceColIndex = cursor.getColumnIndex("price");

            do {
                //Add record to list
                records.add(new Record(cursor.getInt(idColIndex),
                        cursor.getString(titleColIndex),
                        Integer.toString(cursor.getInt(categoryColIndex)),
                        cursor.getString(priceColIndex)));
            } while (cursor.moveToNext());
        }

        db.close();

        listView.setAdapter(new RecordAdapter(activity, records, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.delete(Constants.TABLE_RECORDS, "id=?",
                        new String[] {Integer.toString(records.get((Integer)v.getTag()).getId())});
                db.close();
                retrieveDataFromDB();
            }
        }));
        ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
    }
}
