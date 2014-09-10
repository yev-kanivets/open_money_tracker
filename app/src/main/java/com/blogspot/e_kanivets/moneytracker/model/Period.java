package com.blogspot.e_kanivets.moneytracker.model;

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
}
