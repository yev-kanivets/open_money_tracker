package com.blogspot.e_kanivets.moneytracker.helper;

import com.blogspot.e_kanivets.moneytracker.model.Period;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Observable;

/**
 * Helper class for Money Tracker application. Singleton.
 * Created on 01/09/14.
 *
 * @author Evgenii Kanivets
 */
public class MtHelper extends Observable {

    private static MtHelper mtHelper;

    private Period period;

    public static MtHelper getInstance() {
        if (mtHelper == null) {
            mtHelper = new MtHelper();
        }
        return mtHelper;
    }

    private MtHelper() {
        initPeriod();
    }

    public void update() {
        //notify observers
        setChanged();
        notifyObservers();
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    private void initPeriod() {
        // get today and clear time of day
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);

        // set first day of week
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());

        Date first = cal.getTime();

        cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);

        // set first day of week
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek() + 6);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);

        Date last = cal.getTime();

        period = new Period(first, last);
    }

    public String getFirstDay() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(period.getFirst());
    }

    public String getLastDay() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(period.getLast());
    }
}