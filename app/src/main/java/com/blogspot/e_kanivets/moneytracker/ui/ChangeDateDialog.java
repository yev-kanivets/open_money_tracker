package com.blogspot.e_kanivets.moneytracker.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.DatePicker;

import com.blogspot.e_kanivets.moneytracker.R;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created on 10/09/14.
 *
 * @author Evgenii Kanivets
 */
public class ChangeDateDialog extends AlertDialog{

    private Date date;
    private OnDateChangedListener listener;

    @BindView(R.id.datePicker)
    DatePicker datePicker;

    public ChangeDateDialog(Context context, Date date, OnDateChangedListener listener) {
        super(context);
        this.date = date;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_change_date);
        ButterKnife.bind(ChangeDateDialog.this);

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        datePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), null);
    }

    @OnClick(R.id.b_ok)
    public void ok() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, datePicker.getYear());
        cal.set(Calendar.MONTH, datePicker.getMonth());
        cal.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());

        listener.OnDataChanged(cal.getTime());
        dismiss();
    }

    @OnClick(R.id.b_cancel)
    public void cancel() {
        dismiss();
    }

    public interface OnDateChangedListener {
        void OnDataChanged(Date date);
    }
}