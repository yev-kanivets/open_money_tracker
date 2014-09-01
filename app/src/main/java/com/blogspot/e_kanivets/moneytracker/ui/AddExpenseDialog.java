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
import com.blogspot.e_kanivets.moneytracker.util.Constants;
import com.blogspot.e_kanivets.moneytracker.util.MTApp;

/**
 * Created by eugene on 29/08/14.
 */
public class AddExpenseDialog extends AlertDialog {

    private Context context;
    private DBHelper dbHelper;

    public AddExpenseDialog(Context context) {
        super(context);
        this.context = context;

        dbHelper = MTApp.get().getDbHelper();
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
                //Init variables for inserting record to DB
                ContentValues contentValues = new ContentValues();

                String title = ((EditText) findViewById(R.id.et_title)).getText().toString();
                String category = ((EditText) findViewById(R.id.et_category)).getText().toString();
                String price = ((EditText) findViewById(R.id.et_price)).getText().toString();

                SQLiteDatabase db = dbHelper.getWritableDatabase();

                contentValues.put("type", "1");
                contentValues.put("title", title);
                contentValues.put("category_id", 1);
                contentValues.put("price", price);

                long rowId = db.insert(Constants.TABLE_RECORDS, null, contentValues);

                Log.d(Constants.TAG, "rowId = " + rowId);

                db.close();

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
