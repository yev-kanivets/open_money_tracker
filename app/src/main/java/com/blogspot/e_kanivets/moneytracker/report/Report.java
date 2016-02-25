package com.blogspot.e_kanivets.moneytracker.report;

import android.support.annotation.NonNull;

import com.blogspot.e_kanivets.moneytracker.model.Period;
import com.blogspot.e_kanivets.moneytracker.model.Record;
import com.blogspot.e_kanivets.moneytracker.report.base.IExchangeRateProvider;
import com.blogspot.e_kanivets.moneytracker.report.base.IReport;
import com.blogspot.e_kanivets.moneytracker.report.model.CategoryRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * First {@link IReport} implementation.
 * Created on 2/25/16.
 *
 * @author Evgenii Kanivets
 */
public class Report implements IReport {
    @SuppressWarnings("unused")
    private static final String TAG = "Report";

    private String currency;
    private Period period;
    private List<Record> recordList;
    private IExchangeRateProvider rateProvider;

    public Report(String currency, Period period, List<Record> recordList, IExchangeRateProvider rateProvider) {
        if (currency == null || period == null || recordList == null || rateProvider == null)
            throw new NullPointerException("Params can't be null");

        this.currency = currency;
        this.period = period;
        this.recordList = recordList;
        this.rateProvider = rateProvider;
    }

    @NonNull
    @Override
    public String getCurrency() {
        return currency;
    }

    @NonNull
    @Override
    public Period getPeriod() {
        return period;
    }

    @Override
    public double getTotal() {
        return 0;
    }

    @Override
    public double getTotalIncome() {
        return 0;
    }

    @Override
    public double getTotalExpense() {
        return 0;
    }

    @NonNull
    @Override
    public List<CategoryRecord> getSummary() {
        return new ArrayList<>();
    }
}