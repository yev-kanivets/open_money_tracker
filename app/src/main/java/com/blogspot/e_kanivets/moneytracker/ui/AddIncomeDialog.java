package com.blogspot.e_kanivets.moneytracker.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.helper.DBHelper;
import com.blogspot.e_kanivets.moneytracker.helper.MTHelper;
import com.blogspot.e_kanivets.moneytracker.util.Constants;
import com.blogspot.e_kanivets.moneytracker.util.MTApp;

/**
 * Created by eugene on 29/08/14.
 */
public class AddIncomeDialog extends AlertDialog {

    private Context context;

    public AddIncomeDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_add_record);

        TextView tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTitle.setText(R.string.income);
        tvTitle.setBackgroundColor(context.getResources().getColor(R.color.green_light));

        Button buttonAdd = (Button) findViewById(R.id.b_add);
        buttonAdd.setText(context.getResources().getString(R.string.add_income));
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = ((EditText) findViewById(R.id.et_title)).getText().toString();
                String category = ((EditText) findViewById(R.id.et_category)).getText().toString();
                int price = Integer.parseInt(((EditText) findViewById(R.id.et_price)).getText().toString());

                MTHelper.getInstance().addRecord(0, 0, title, category, price);

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

        //Horrible thing to show a software keyboard
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        setOnShowListener(new OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(findViewById(R.id.et_title), InputMethodManager.SHOW_IMPLICIT);
            }
        });
    }
}
