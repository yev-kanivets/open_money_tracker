package com.blogspot.e_kanivets.moneytracker.report.model;

import android.support.annotation.NonNull;

import com.blogspot.e_kanivets.moneytracker.entity.Record;

import java.util.ArrayList;
import java.util.List;

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
    private List<Record> recordList;

    public SummaryRecord(String title, String currency, double amount) {
        this.title = title;
        this.currency = currency;
        this.amount = amount;
        recordList = new ArrayList<>();
    }

    public void add(@NonNull Record record) {
        recordList.add(record);
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

    public List<Record> getRecordList() {
        return recordList;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof SummaryRecord) {
            SummaryRecord summaryRecord = (SummaryRecord) o;
            return this.currency.equals(summaryRecord.getCurrency())
                    && this.amount == summaryRecord.getAmount()
                    && this.recordList.equals(summaryRecord.getRecordList());
        } else return false;
    }
}