package com.blogspot.e_kanivets.moneytracker.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.helper.DBHelper;
import com.blogspot.e_kanivets.moneytracker.helper.MTHelper;
import com.blogspot.e_kanivets.moneytracker.util.Constants;
import com.blogspot.e_kanivets.moneytracker.util.MTApp;

/**
 * Created by eugene on 29/08/14.
 */
public class AddExpenseDialog extends AlertDialog {

    private Context context;

    public AddExpenseDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_add_record);

        TextView tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTitle.setText(R.string.expense);
        tvTitle.setBackgroundColor(context.getResources().getColor(R.color.red_light));

        Button buttonAdd = (Button) findViewById(R.id.b_add);
        buttonAdd.setText(context.getResources().getString(R.string.add_expense));
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = ((EditText) findViewById(R.id.et_title)).getText().toString();
                String category = ((EditText) findViewById(R.id.et_category)).getText().toString();
                int price = Integer.parseInt(((EditText) findViewById(R.id.et_price)).getText().toString());

                MTHelper.getInstance().addRecord(0, 1, title, 0, price);

                dismiss();
            }
        });

        Button buttonCancel = (Button) findViewById(R.id.b_cancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }


}
