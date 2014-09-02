package com.blogspot.e_kanivets.moneytracker.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.helper.DBHelper;
import com.blogspot.e_kanivets.moneytracker.helper.MTHelper;
import com.blogspot.e_kanivets.moneytracker.util.AppUtils;
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

        //Justify width of dialog
        View view = getLayoutInflater().inflate(R.layout.dialog_add_record, null);
        setContentView(view);

        ViewGroup.LayoutParams params = view.getLayoutParams();
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        params.width = Math.min(metrics.widthPixels-20, AppUtils.scaleValue(context, 700));
        view.setLayoutParams(params);

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

                MTHelper.getInstance().addRecord(0, 1, title, category, price);

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

                //Set width of dialog to match a width of content
                DisplayMetrics metrics = context.getResources().getDisplayMetrics();
                getWindow().setLayout(Math.min(metrics.widthPixels-20, AppUtils.scaleValue(context, 700)), LinearLayout.LayoutParams.WRAP_CONTENT);
                getWindow().setGravity(Gravity.CENTER_HORIZONTAL);
            }
        });
    }
}
