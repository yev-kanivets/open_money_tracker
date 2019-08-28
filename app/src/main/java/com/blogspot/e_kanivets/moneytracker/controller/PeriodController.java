package com.blogspot.e_kanivets.moneytracker.controller;

import com.blogspot.e_kanivets.moneytracker.entity.Period;

import java.util.Calendar;
import java.util.Date;

/**
 * Controller class to encapsulate {@link Period} handling logic.
 * Not deal with {@link com.blogspot.e_kanivets.moneytracker.repo.base.IRepo} instances as others.
 * Created on 4/21/16.
 *
 * @author Evgenii Kanivets
 */
public class PeriodController {
    private PreferenceController preferenceController;

    public PeriodController(PreferenceController preferenceController) {
        this.preferenceController = preferenceController;
    }

    public Period readLastUsedPeriod() {
        long first = preferenceController.readFirstTs();
        long last = preferenceController.readLastTs();
        String type = preferenceController.readPeriodType();

        if (first == -1 || last == -1 || type == null) return weekPeriod();
        else {
            switch (type) {
                case Period.TYPE_DAY:
                    return dayPeriod();

                case Period.TYPE_WEEK:
                    return weekPeriod();

                case Period.TYPE_MONTH:
                    return monthPeriod();

                case Period.TYPE_YEAR:
                    return yearPeriod();

                case Period.TYPE_ALL_TIME:
                    return allTimePeriod();

                case Period.TYPE_CUSTOM:
                    return weekPeriod();

                default:
                    return weekPeriod();

            }
        }
    }

    public void writeLastUsedPeriod(Period period) {
        preferenceController.writeFirstTs(period.getFirst().getTime());
        preferenceController.writeLastTs(period.getLast().getTime());
        preferenceController.writePeriodType(period.getType());
    }

    public Period dayPeriod() {
        Calendar cal = Calendar.getInstance();

        //set start of day
        setDayStart(cal);

        Date first = cal.getTime();

        // set end of day
        setDayEnd(cal);

        Date last = cal.getTime();

        return new Period(first, last, Period.TYPE_DAY);
    }

    public Period weekPeriod() {
        Calendar cal = Calendar.getInstance();

        // set start of week
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        setDayStart(cal);

        Date first = cal.getTime();

        // set last day of week
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek() + 6);
        setDayEnd(cal);

        Date last = cal.getTime();

        return new Period(first, last, Period.TYPE_WEEK);
    }

    public Period monthPeriod() {
        Calendar cal = Calendar.getInstance();

        // set start of month
        cal.set(Calendar.DAY_OF_MONTH, 1);
        setDayStart(cal);

        Date first = cal.getTime();

        // set end of month
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        setDayEnd(cal);

        Date last = cal.getTime();

        return new Period(first, last, Period.TYPE_MONTH);
    }

    public Period yearPeriod() {
        Calendar cal = Calendar.getInstance();

        // set start of year
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        setDayStart(cal);

        Date first = cal.getTime();

        // set end of year
        cal.set(Calendar.MONTH, Calendar.DECEMBER);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        setDayEnd(cal);

        Date last = cal.getTime();

        return new Period(first, last, Period.TYPE_YEAR);
    }

    public Period allTimePeriod() {
        Calendar cal = Calendar.getInstance();

        // set start of time by Jesus
        cal.set(Calendar.YEAR, 2000);
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        setDayStart(cal);

        Date first = cal.getTime();

        // set possible end of time
        cal.set(Calendar.YEAR, 3000);
        cal.set(Calendar.MONTH, Calendar.DECEMBER);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        setDayEnd(cal);

        Date last = cal.getTime();

        return new Period(first, last, Period.TYPE_ALL_TIME);
    }

    private void setDayStart(Calendar cal) {
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
    }

    private void setDayEnd(Calendar cal) {
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
    }
}
