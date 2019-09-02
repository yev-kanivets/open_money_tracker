package com.blogspot.e_kanivets.moneytracker.report.record.model;

import android.support.annotation.NonNull;

/**
 * Entity class.
 * Created on 2/25/16.
 *
 * @author Evgenii Kanivets
 */
public class SummaryRecord {
    private String title;
    private String currency;
    private double amount;

    public SummaryRecord(String title, String currency, double amount, int recordsCount) {
        this.title = makeTitle(title, recordsCount);
        this.currency = currency;
        this.amount = amount;
    }

    public String getTitle() {
        return title;
    }

    public String getCurrency() {
        return currency;
    }

    public double getAmount() {
        return amount;
    }

    private String makeTitle(String title, int recordsCount) {
        if (recordsCount <= 1) return title;
        else return title + " (" + recordsCount + ")";
    }

}