package com.blogspot.e_kanivets.moneytracker.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Not data entity that's used for {@link com.blogspot.e_kanivets.moneytracker.adapter.ExchangeRateAdapter}.
 * Created on 7/13/16.
 *
 * @author Evgenii Kanivets
 */
public class ExchangeRatePair implements Parcelable {
    private String fromCurrency;
    private String toCurrency;
    private double amountBuy;
    private double amountSell;

    public ExchangeRatePair() {
    }

    protected ExchangeRatePair(Parcel in) {
        fromCurrency = in.readString();
        toCurrency = in.readString();
        amountBuy = in.readDouble();
        amountSell = in.readDouble();
    }

    public static final Creator<ExchangeRatePair> CREATOR = new Creator<ExchangeRatePair>() {
        @Override
        public ExchangeRatePair createFromParcel(Parcel in) {
            return new ExchangeRatePair(in);
        }

        @Override
        public ExchangeRatePair[] newArray(int size) {
            return new ExchangeRatePair[size];
        }
    };

    public String getFromCurrency() {
        return fromCurrency;
    }

    public String getToCurrency() {
        return toCurrency;
    }

    public double getAmountBuy() {
        return amountBuy;
    }

    public double getAmountSell() {
        return amountSell;
    }

    public void setFromCurrency(String fromCurrency) {
        this.fromCurrency = fromCurrency;
    }

    public void setToCurrency(String toCurrency) {
        this.toCurrency = toCurrency;
    }

    public void setAmountBuy(double amountBuy) {
        this.amountBuy = amountBuy;
    }

    public void setAmountSell(double amountSell) {
        this.amountSell = amountSell;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fromCurrency);
        dest.writeString(toCurrency);
        dest.writeDouble(amountBuy);
        dest.writeDouble(amountSell);
    }
}
