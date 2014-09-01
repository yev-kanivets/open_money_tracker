package com.blogspot.e_kanivets.moneytracker.activity;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.helper.DBHelper;
import com.blogspot.e_kanivets.moneytracker.ui.AddExpenseDialog;
import com.blogspot.e_kanivets.moneytracker.ui.AddIncomeDialog;
import com.blogspot.e_kanivets.moneytracker.util.MTApp;


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

        //Link views
        btnAddIncome = (Button) findViewById(R.id.b_add_income);
        btnAddExpense = (Button) findViewById(R.id.b_add_expense);

        listView = (ListView) findViewById(R.id.listView);

        //Set listeners
        btnAddIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AddIncomeDialog(activity).show();
            }
        });

        btnAddExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AddExpenseDialog(activity).show();
            }
        });

        dbHelper = MTApp.get().getDbHelper();
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
}
