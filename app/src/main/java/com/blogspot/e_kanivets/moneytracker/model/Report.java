package com.blogspot.e_kanivets.moneytracker.model;

import android.util.Pair;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.util.MTApp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by eugene on 11/09/14.
 */
public class Report {

    private List<Record> records;
    private List<Pair<String, Integer>> reportList;
    private List<Pair<String, Integer>> summaryReportList;

    public Report(List<Record> records) {
        this.records = records;
        makeReport();
    }

    public List<Pair<String, Integer>> getReportList() {
        return reportList;
    }

    public List<Pair<String, Integer>> getSummaryReportList() {
        return summaryReportList;
    }

    private void makeReport() {
        HashMap<String, Integer> map = new HashMap<String, Integer>();

        // Calculate total of all records
        int totalIncome = 0, totalExpense = 0;

        for(Record record : records) {
            int sum = 0;
            //If category has already exist add to it
            if(map.containsKey(record.getCategory())) {
                sum = map.get(record.getCategory());
            }

            //Add or subtract price
            if(record.isIncome()) {
                totalIncome += record.getPrice();
                map.put(record.getCategory(), sum + record.getPrice());
            } else {
                totalExpense -= record.getPrice();
                map.put(record.getCategory(), sum - record.getPrice());
            }
        }

        //Sort reportList
        List<Pair<String, Integer>> reportIncomes = new ArrayList<Pair<String, Integer>>();
        List<Pair<String, Integer>> reportExpenses = new ArrayList<Pair<String, Integer>>();

        for(String name : map.keySet()) {
            if(map.get(name) > 0) {
                reportIncomes.add(new Pair<String, Integer>(name, map.get(name)));
            }
            else {
                reportExpenses.add(new Pair<String, Integer>(name, map.get(name)));
            }
        }

        sortList(reportIncomes);
        sortList(reportExpenses);

        //Added incomes and expenses to ArrayList
        reportList = new ArrayList<Pair<String, Integer>>();
        reportList.addAll(reportIncomes);
        reportList.addAll(reportExpenses);

        //Add summary row to list
        summaryReportList = new ArrayList<Pair<String, Integer>>();
        summaryReportList.add(new Pair<String, Integer>(
                MTApp.get().getResources().getString(R.string.total_incomes) + " :", totalIncome));
        summaryReportList.add(new Pair<String, Integer>(
                MTApp.get().getResources().getString(R.string.total_expenses) + " :", totalExpense));
        summaryReportList.add(new Pair<String, Integer>(
                MTApp.get().getResources().getString(R.string.total) + " :", totalExpense + totalIncome));
    }

    private void sortList(List<Pair<String, Integer>> list) {
        int n = list.size();

        for(int i=0;i<n-1;i++) {
            for(int j=0;j<n-i-1;j++) {
                if(Math.abs(list.get(j).second) < Math.abs(list.get(j+1).second)) {
                    Pair<String, Integer> tmp = list.get(j);
                    list.set(j, list.get(j+1));
                    list.set(j+1, tmp);
                }
            }
        }
    }
}
