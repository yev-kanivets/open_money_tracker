package com.blogspot.e_kanivets.moneytracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

    private Activity activity;

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

        //Set listeners
        btnAddIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddIncomeDialog();
            }
        });

        btnAddExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddExpenseDialog();
            }
        });
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

    private void showAddExpenseDialog() {
        View layout = getLayoutInflater().inflate(R.layout.dialog_add_record, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(layout);

        final AlertDialog dialog = builder.show();

        TextView tvTitle = (TextView) layout.findViewById(R.id.tv_title);
        tvTitle.setText(R.string.expense);
        tvTitle.setBackgroundColor(getResources().getColor(R.color.red_light));

        Button buttonAdd = (Button) layout.findViewById(R.id.b_add);
        buttonAdd.setText(getResources().getString(R.string.add_expense));
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Button buttonCancel = (Button) layout.findViewById(R.id.b_cancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void showAddIncomeDialog() {
        View layout = getLayoutInflater().inflate(R.layout.dialog_add_record, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(layout);

        final AlertDialog dialog = builder.show();

        TextView tvTitle = (TextView) layout.findViewById(R.id.tv_title);
        tvTitle.setText(R.string.income);
        tvTitle.setBackgroundColor(getResources().getColor(R.color.green_light));

        Button buttonAdd = (Button) layout.findViewById(R.id.b_add);
        buttonAdd.setText(getResources().getString(R.string.add_income));
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Button buttonCancel = (Button) layout.findViewById(R.id.b_cancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}
