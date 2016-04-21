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

        return new Period(first, last, Period.TYPE_DAY);
    }

    public Period weekPeriod() {
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

        return new Period(first, last, Period.TYPE_WEEK);
    }

    public Period monthPeriod() {
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

        return new Period(first, last, Period.TYPE_MONTH);
    }

    public Period yearPeriod() {
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

        return new Period(first, last, Period.TYPE_YEAR);
    }
}
