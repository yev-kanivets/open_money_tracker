package com.blogspot.e_kanivets.moneytracker.entity;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Entity class for Period which consists from two dates. Immutable.
 * Created on 10/09/14.
 *
 * @author Evgenii Kanivets
 */
public class Period implements Parcelable {

    public static final String TYPE_DAY = "day";
    public static final String TYPE_WEEK = "week";
    public static final String TYPE_MONTH = "month";
    public static final String TYPE_YEAR = "year";
    public static final String TYPE_ALL_TIME = "all_time";
    public static final String TYPE_CUSTOM = "custom";

    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM, yyyy");

    private Date first;
    private Date last;
    private String type;

    public Period(Date first, Date last, String type) {
        this.first = new Date(first.getTime());
        this.last = new Date(last.getTime());
        this.type = type;
    }

    protected Period(Parcel in) {
        first = new Date(in.readLong());
        last = new Date(in.readLong());
        type = in.readString();
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

    public String getType() {
        return type;
    }

    public String getFirstDay() {
        return dateFormat.format(getFirst());
    }

    public String getLastDay() {
        return dateFormat.format(getLast());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(first.getTime());
        dest.writeLong(last.getTime());
        dest.writeString(type);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Period) {
            Period period = (Period) o;
            return this.first.equals(period.getFirst())
                    && this.last.equals(period.getLast());
        } else return false;
    }
}