package com.blogspot.e_kanivets.moneytracker.report;

import android.support.annotation.NonNull;

import com.blogspot.e_kanivets.moneytracker.entity.data.ExchangeRate;
import com.blogspot.e_kanivets.moneytracker.entity.Period;
import com.blogspot.e_kanivets.moneytracker.entity.data.Record;
import com.blogspot.e_kanivets.moneytracker.report.base.IExchangeRateProvider;
import com.blogspot.e_kanivets.moneytracker.report.base.IReport;
import com.blogspot.e_kanivets.moneytracker.report.model.CategoryRecord;
import com.blogspot.e_kanivets.moneytracker.report.model.SummaryRecord;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
    private List<CategoryRecord> categoryRecordList;

    public Report(String currency, Period period, List<Record> recordList, IExchangeRateProvider rateProvider) {
        if (currency == null || period == null || recordList == null || rateProvider == null)
            throw new NullPointerException("Params can't be null");

        this.currency = currency;
        this.period = period;
        this.rateProvider = rateProvider;

        categoryRecordList = new ArrayList<>();

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
        return categoryRecordList;
    }

    private void makeReport(List<Record> recordList) {
        totalIncome = 0;
        totalExpense = 0;
        categoryRecordList.clear();

        List<Record> convertedRecordList = convertRecordList(recordList);

        Map<String, List<Record>> categorySortedMap = new TreeMap<>();

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

            String categoryName = null;
            if (record.getCategory() != null) categoryName = record.getCategory().getName();

            if (categoryName != null && !categorySortedMap.containsKey(categoryName))
                categorySortedMap.put(categoryName, new ArrayList<Record>());
            categorySortedMap.get(categoryName).add(record);
        }

        for (String category : categorySortedMap.keySet()) {
            categoryRecordList.add(createCategoryRecord(category, categorySortedMap.get(category)));
        }

        Collections.sort(categoryRecordList, new Comparator<CategoryRecord>() {
            @Override
            public int compare(CategoryRecord lhs, CategoryRecord rhs) {
                return compareDoubles(lhs.getAmount(), rhs.getAmount());
            }
        });
    }

    @NonNull
    private List<Record> convertRecordList(List<Record> recordList) {
        List<Record> convertedRecordList = new ArrayList<>();

        for (Record record : recordList) {
            double convertedPrice = record.getPrice();

            if (!currency.equals(record.getCurrency())) {
                ExchangeRate exchangeRate = rateProvider.getRate(record);
                if (exchangeRate == null) throw new NullPointerException("No exchange rate found");
                convertedPrice *= exchangeRate.getAmount();
            }

            Record convertedRecord = new Record(record.getId(), record.getTime(), record.getType(),
                    record.getTitle(), record.getCategory(), convertedPrice, record.getAccount(), currency);

            convertedRecordList.add(convertedRecord);
        }

        return convertedRecordList;
    }

    @NonNull
    private CategoryRecord createCategoryRecord(String category, List<Record> recordList) {
        Map<String, List<Record>> titleSortedMap = new TreeMap<>();

        double amount = 0;

        for (Record record : recordList) {
            amount += getAmount(record);

            String title = record.getTitle();

            if (!titleSortedMap.containsKey(title))
                titleSortedMap.put(title, new ArrayList<Record>());
            titleSortedMap.get(title).add(record);
        }

        CategoryRecord categoryRecord = new CategoryRecord(category, currency, amount);

        for (String title : titleSortedMap.keySet()) {
            categoryRecord.add(createSummaryRecord(title, titleSortedMap.get(title)));
        }

        Collections.sort(categoryRecord.getSummaryRecordList(), new Comparator<SummaryRecord>() {
            @Override
            public int compare(SummaryRecord lhs, SummaryRecord rhs) {
                return compareDoubles(lhs.getAmount(), rhs.getAmount());
            }
        });

        return categoryRecord;
    }

    @NonNull
    private SummaryRecord createSummaryRecord(String title, List<Record> recordList) {
        double amount = 0;

        for (Record record : recordList) {
            amount += getAmount(record);
        }

        SummaryRecord summaryRecord = new SummaryRecord(title, currency, amount);

        for (Record record : recordList) {
            summaryRecord.add(record);
        }

        Collections.sort(summaryRecord.getRecordList(), new Comparator<Record>() {
            @Override
            public int compare(Record lhs, Record rhs) {
                return compareDoubles(getAmount(lhs), getAmount(rhs));
            }
        });

        return summaryRecord;
    }

    private double getAmount(Record record) {
        switch (record.getType()) {
            case Record.TYPE_INCOME:
                return record.getPrice();

            case Record.TYPE_EXPENSE:
                return -record.getPrice();

            default:
                return 0;
        }
    }

    private int compareDoubles(double lhs, double rhs) {
        if (lhs > 0 && rhs < 0) return -1;
        else if (lhs < 0 && rhs > 0) return 1;
        else return -1 * Double.compare(Math.abs(lhs), Math.abs(rhs));
    }
}