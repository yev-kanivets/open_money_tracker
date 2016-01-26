package com.blogspot.e_kanivets.moneytracker.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;
import java.util.Date;

/**
 * Entity class for Period which consists from two dates.
 * Created on 10/09/14.
 *
 * @author Evgenii Kanivets
 */
public class Period implements Parcelable {
    private Date first;
    private Date last;

    public Period(Date first, Date last) {
        this.first = first;
        this.last = last;
    }

    protected Period(Parcel in) {
        first = new Date(in.readLong());
        last = new Date(in.readLong());
    }

    public static final Creator<Period> CREATOR = new Creator<Period>() {
        @Override
        public Period createFromParcel(Parcel in) {
            return new Period(in);
        }

        @Override
        public Period[] newArray(int size) {
            return new Period[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(first.getTime());
        dest.writeLong(last.getTime());
    }
}
