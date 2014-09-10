package com.blogspot.e_kanivets.moneytracker.model;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by eugene on 10/09/14.
 */
public class Period {
    private Date first;
    private Date last;

    public Period(Date first, Date last) {
        this.first = first;
        this.last = last;
    }

    public Date getFirst() {
        return first;
    }

    public Date getLast() {
        return last;
    }

    public void setFirst(Date first) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(first);

        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);

        this.first = cal.getTime();
    }

    public void setLast(Date last) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(last);

        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);

        this.last = cal.getTime();
    }
}
