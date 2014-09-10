package com.blogspot.e_kanivets.moneytracker.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import com.blogspot.e_kanivets.moneytracker.R;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by eugene on 10/09/14.
 */
public class ChangeDateDialog extends AlertDialog{

    private Date date;
    private OnDateChangedListener listener;

    private DatePicker datePicker;
    private Button btnOk;
    private Button btnCancel;

    public ChangeDateDialog(Context context, Date date, OnDateChangedListener listener) {
        super(context);
        this.date = date;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_change_date);

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        datePicker = (DatePicker) findViewById(R.id.datePicker);
        btnOk = (Button) findViewById(R.id.b_ok);
        btnCancel = (Button) findViewById(R.id.b_cancel);

        datePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), null);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, datePicker.getYear());
                cal.set(Calendar.MONTH, datePicker.getMonth());
                cal.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());

                listener.OnDataChanged(cal.getTime());
                dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    public interface OnDateChangedListener {
        public void OnDataChanged(Date date);
    }
}
