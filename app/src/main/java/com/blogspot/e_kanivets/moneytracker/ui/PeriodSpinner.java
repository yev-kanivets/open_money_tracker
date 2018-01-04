package com.blogspot.e_kanivets.moneytracker.ui;

import android.content.Context;
import android.support.v7.widget.AppCompatSpinner;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.blogspot.e_kanivets.moneytracker.MtApp;
import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.controller.PeriodController;
import com.blogspot.e_kanivets.moneytracker.entity.Period;

import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

/**
 * Custom Spinner view to encapsulate a Period logic.
 * Created on 3/16/16.
 *
 * @author Evgenii Kanivets
 */
public class PeriodSpinner extends AppCompatSpinner {
    private Context context;

    @Inject
    PeriodController periodController;

    private OnPeriodSelectedListener periodSelectedListener;
    private AdapterView.OnItemSelectedListener listener;
    private Period lastPeriod;

    public PeriodSpinner(Context context) {
        super(context);
        init(context);
    }

    public PeriodSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PeriodSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void setPeriod(Period period) {
        if (lastPeriod != null && lastPeriod.equals(period)) return;
        if (periodSelectedListener != null) periodSelectedListener.onPeriodSelected(period);

        lastPeriod = period;

        switch (period.getType()) {
            case Period.TYPE_DAY:
                setSelection(0);
                break;

            case Period.TYPE_WEEK:
                setSelection(1);
                break;

            case Period.TYPE_MONTH:
                setSelection(2);
                break;

            case Period.TYPE_YEAR:
                setSelection(3);
                break;
        }
    }

    public void setPeriodSelectedListener(OnPeriodSelectedListener periodSelectedListener) {
        this.periodSelectedListener = periodSelectedListener;
    }

    @Override
    public void setSelection(int position) {
        super.setSelection(position);
        if (listener != null) listener.onItemSelected(null, null, position, 0);
    }

    public void setOnItemSelectedEvenIfUnchangedListener(OnItemSelectedListener listener) {
        this.listener = listener;
    }

    private void init(Context context) {
        this.context = context;
        MtApp.get().getAppComponent().inject(PeriodSpinner.this);

        setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.array_periods)));
        setOnItemSelectedEvenIfUnchangedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Calendar calendar = Calendar.getInstance();
                calendar.clear(Calendar.MINUTE);
                calendar.clear(Calendar.SECOND);
                calendar.clear(Calendar.MILLISECOND);

                switch (position) {
                    case 0:
                        setPeriod(periodController.dayPeriod());
                        break;

                    case 1:
                        setPeriod(periodController.weekPeriod());
                        break;

                    case 2:
                        setPeriod(periodController.monthPeriod());
                        break;

                    case 3:
                        setPeriod(periodController.yearPeriod());
                        break;

                    case 4:
                        setPeriod(periodController.allTimePeriod());
                        break;

                    case 5:
                        // Custom period selection
                        showFromDateDialog();
                        break;

                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void showFromDateDialog() {
        if (lastPeriod == null) return;
        ChangeDateDialog dialog = new ChangeDateDialog(context, lastPeriod.getFirst(),
                new ChangeDateDialog.OnDateChangedListener() {
                    @Override
                    public void OnDataChanged(Date fromDate) {
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(fromDate);
                        cal.set(Calendar.HOUR_OF_DAY, 0);
                        cal.set(Calendar.MINUTE, 0);
                        cal.set(Calendar.SECOND, 0);
                        cal.set(Calendar.MILLISECOND, 0);

                        showToDateDialog(cal.getTime());
                    }
                });
        dialog.show();
    }

    private void showToDateDialog(final Date fromDate) {
        if (lastPeriod == null) return;

        ChangeDateDialog dialog = new ChangeDateDialog(context, lastPeriod.getLast(),
                new ChangeDateDialog.OnDateChangedListener() {
                    @Override
                    public void OnDataChanged(Date toDate) {
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(toDate);
                        cal.set(Calendar.HOUR_OF_DAY, 23);
                        cal.set(Calendar.MINUTE, 59);
                        cal.set(Calendar.SECOND, 59);
                        cal.set(Calendar.MILLISECOND, 999);

                        setPeriod(new Period(fromDate, cal.getTime(), Period.TYPE_CUSTOM));
                    }
                });
        dialog.show();
    }

    public interface OnPeriodSelectedListener {
        void onPeriodSelected(Period period);
    }
}
