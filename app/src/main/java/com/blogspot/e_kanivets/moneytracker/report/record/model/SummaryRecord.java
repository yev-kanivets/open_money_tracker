package com.blogspot.e_kanivets.moneytracker.report.record.model;

import com.blogspot.e_kanivets.moneytracker.MtApp;
import com.blogspot.e_kanivets.moneytracker.R;

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
        this.title = buildTitle(title, recordsCount);
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

    private String buildTitle(String title, int recordsCount) {
        if (recordsCount <= 1) return title;
        else
            return MtApp.get().getResources().getString(R.string.title_summary_record, title, recordsCount);
    }

}