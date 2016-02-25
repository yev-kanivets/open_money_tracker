package com.blogspot.e_kanivets.moneytracker.report;

import android.support.annotation.NonNull;

import com.blogspot.e_kanivets.moneytracker.model.ExchangeRate;
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
    private IExchangeRateProvider rateProvider;

    private double totalIncome;
    private double totalExpense;

    public Report(String currency, Period period, List<Record> recordList, IExchangeRateProvider rateProvider) {
        if (currency == null || period == null || recordList == null || rateProvider == null)
            throw new NullPointerException("Params can't be null");

        this.currency = currency;
        this.period = period;
        this.rateProvider = rateProvider;

        makeReport(recordList);
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
        return getTotalExpense() + getTotalIncome();
    }

    @Override
    public double getTotalIncome() {
        return totalIncome;
    }

    @Override
    public double getTotalExpense() {
        return totalExpense;
    }

    @NonNull
    @Override
    public List<CategoryRecord> getSummary() {
        return new ArrayList<>();
    }

    private void makeReport(List<Record> recordList) {
        totalIncome = 0;
        totalExpense = 0;

        List<Record> convertedRecordList = convertRecordList(recordList);

        for (Record record : convertedRecordList) {
            switch (record.getType()) {
                case Record.TYPE_INCOME:
                    totalIncome += record.getPrice();
                    break;

                case Record.TYPE_EXPENSE:
                    totalExpense -= record.getPrice();
                    break;

                default:
                    break;
            }
        }
    }

    @NonNull
    private List<Record> convertRecordList(List<Record> recordList) {
        List<Record> convertedRecordList = new ArrayList<>();

        for (Record record : recordList) {
            int convertedPrice = record.getPrice();

            if (!currency.equals(record.getCurrency())) {
                ExchangeRate exchangeRate = rateProvider.getRate(record);
                if (exchangeRate == null) throw new NullPointerException("No exchange rate found");
                convertedPrice *= exchangeRate.getAmount();
            }

            Record convertedRecord = new Record(record);
            convertedRecord.setPrice(convertedPrice);

            convertedRecordList.add(convertedRecord);
        }

        return convertedRecordList;
    }
}