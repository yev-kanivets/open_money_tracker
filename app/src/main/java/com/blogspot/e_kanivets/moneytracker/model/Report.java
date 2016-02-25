package com.blogspot.e_kanivets.moneytracker.model;

import android.util.Pair;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.MtApp;
import com.blogspot.e_kanivets.moneytracker.controller.ExchangeRateController;
import com.blogspot.e_kanivets.moneytracker.report.ExchangeRateProvider;
import com.blogspot.e_kanivets.moneytracker.report.base.IExchangeRateProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created on 11/09/14.
 *
 * @author Evgenii Kanivets
 */
public class Report {
    private List<Record> records;
    private String currency;
    private List<Record> summaryRecordList;
    private List<Pair<String, Integer>> reportList;
    private List<Pair<String, Integer>> summaryReportList;

    private IExchangeRateProvider rateProvider;

    public Report(List<Record> records, String currency, ExchangeRateController exchangeRateController) {
        this.records = records;
        this.currency = currency;
        rateProvider = new ExchangeRateProvider(currency, exchangeRateController);

        makeReport();
    }

    public List<Pair<String, Integer>> getReportList() {
        return reportList;
    }

    public List<Pair<String, Integer>> getSummaryReportList() {
        return summaryReportList;
    }

    public List<Record> getSummaryRecordList() {
        return summaryRecordList;
    }

    private void makeReport() {
        HashMap<String, Integer> map = new HashMap<>();
        HashMap<String, Record> recordMap = new HashMap<>();

        // Calculate total of all records
        int totalIncome = 0, totalExpense = 0;

        for (Record record : records) {
            int sum = 0;
            //If category has already exist add to it
            if (map.containsKey(record.getCategory())) {
                sum = map.get(record.getCategory());
            }

            int convertedPrice = record.getPrice();
            if (!currency.equals(record.getCurrency())) {
                ExchangeRate rate = rateProvider.getRate(record);
                if (rate != null) convertedPrice *= rate.getAmount();
            }

            //Add or subtract price
            if (record.isIncome()) {
                totalIncome += convertedPrice;
                map.put(record.getCategory(), sum + convertedPrice);
            } else {
                totalExpense -= convertedPrice;
                map.put(record.getCategory(), sum - convertedPrice);
            }

            String key = record.getCategory() + "/" + record.getTitle();
            Record summaryRecord = recordMap.get(key);
            int price = convertedPrice;

            if (record.isIncome()) {
                price *= -1;
            }

            if (summaryRecord == null) {
                summaryRecord = new Record(-1, -1, -1, record.getTitle(), record.getCategoryId(),
                        price, record.getAccountId(), currency);
            } else {
                summaryRecord.setPrice(summaryRecord.getPrice() + price);
            }

            recordMap.put(key, summaryRecord);
        }

        fillReportList(map);
        fillSummaryReportList(totalIncome, totalExpense);
        fillRecordList(recordMap);
    }

    private void fillReportList(HashMap<String, Integer> map) {
        //Sort reportList
        List<Pair<String, Integer>> reportIncomes = new ArrayList<>();
        List<Pair<String, Integer>> reportExpenses = new ArrayList<>();

        for (String name : map.keySet()) {
            if (map.get(name) > 0) reportIncomes.add(new Pair<>(name, map.get(name)));
            else reportExpenses.add(new Pair<>(name, map.get(name)));
        }

        sortList(reportIncomes);
        sortList(reportExpenses);

        //Added incomes and expenses to ArrayList
        reportList = new ArrayList<>();
        reportList.addAll(reportIncomes);
        reportList.addAll(reportExpenses);
    }

    private void fillSummaryReportList(int totalIncome, int totalExpense) {
        //Add summary row to list
        summaryReportList = new ArrayList<>();
        summaryReportList.add(new Pair<>(
                MtApp.get().getResources().getString(R.string.total_incomes) + " :", totalIncome));
        summaryReportList.add(new Pair<>(
                MtApp.get().getResources().getString(R.string.total_expenses) + " :", totalExpense));
        summaryReportList.add(new Pair<>(
                MtApp.get().getResources().getString(R.string.total) + " :", totalExpense + totalIncome));
    }

    private void fillRecordList(HashMap<String, Record> recordMap) {
        //Sort reportList
        List<Record> recordIncomes = new ArrayList<>();
        List<Record> recordExpenses = new ArrayList<>();

        for (String name : recordMap.keySet()) {
            if (recordMap.get(name).getPrice() > 0) {
                recordIncomes.add(recordMap.get(name));
            } else {
                recordExpenses.add(recordMap.get(name));
            }
        }

        sortRecordList(recordIncomes);
        sortRecordList(recordExpenses);

        //Added incomes and expenses to ArrayList
        summaryRecordList = new ArrayList<>();
        summaryRecordList.addAll(recordIncomes);
        summaryRecordList.addAll(recordExpenses);

        sortRecordList(summaryRecordList);
    }

    private void sortList(List<Pair<String, Integer>> list) {
        int n = list.size();

        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (Math.abs(list.get(j).second) < Math.abs(list.get(j + 1).second)) {
                    Pair<String, Integer> tmp = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, tmp);
                }
            }
        }
    }

    private void sortRecordList(List<Record> list) {
        int n = list.size();

        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (Math.abs(list.get(j).getPrice()) < Math.abs(list.get(j + 1).getPrice())) {
                    Record tmp = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, tmp);
                }
            }
        }
    }
}