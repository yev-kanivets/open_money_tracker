package com.blogspot.e_kanivets.moneytracker.helper;

import com.blogspot.e_kanivets.moneytracker.model.Period;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by fess on 15.11.2014.
 */
public class PeriodHelper {

    private static PeriodHelper periodHelper;

    public static PeriodHelper getInstance() {
        if(periodHelper == null) {
            periodHelper = new PeriodHelper();
        }

        return periodHelper;
    }

    private PeriodHelper() {}

    public Period getDayPeriod() {
        //set start of day
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        Date first = cal.getTime();

        //set end of day
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);

        Date last = cal.getTime();

        return new Period(first, last);
    }

    public Period getWeekPeriod() {
        // set start of week
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        // set first day of week
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());

        Date first = cal.getTime();

        // set last day of week
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek() + 6);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);

        Date last = cal.getTime();

        return new Period(first, last);
    }

    public Period getMonthPeriod() {
        //set start of month
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        cal.set(Calendar.DAY_OF_MONTH, 1);

        Date first = cal.getTime();

        //set end of month
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);

        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));

        Date last = cal.getTime();

        return new Period(first, last);
    }

    public Period getYearPeriod() {
        //set start of year
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.DAY_OF_MONTH, 1);

        Date first = cal.getTime();

        //set end of year
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);

        cal.set(Calendar.MONTH, Calendar.DECEMBER);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));

        Date last = cal.getTime();

        return new Period(first, last);
    }
}
