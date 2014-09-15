package com.blogspot.e_kanivets.moneytracker.model;

import android.util.Pair;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.util.MTApp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by eugene on 11/09/14.
 */
public class Report {

    private List<Record> records;
    private List<Pair<String, Integer>> reportList;

    public Report(List<Record> records) {
        this.records = records;
        makeReport();
    }

    public List<Pair<String, Integer>> getReportList() {
        return reportList;
    }

    private void makeReport() {
        HashMap<String, Integer> map = new HashMap<String, Integer>();

        for(Record record : records) {
            int sum = 0;
            //If category has already exist add to it
            if(map.containsKey(record.getCategory())) {
                sum = map.get(record.getCategory());
            }

            //Add or subtract price
            if(record.isIncome()) {
                map.put(record.getCategory(), sum + record.getPrice());
            } else {
                map.put(record.getCategory(), sum - record.getPrice());
            }
        }

        // Calculate total of all records
        int totalIncome = 0, totalExpense = 0;

        //Convert HashMap to ArrayList
        reportList = new ArrayList<Pair<String, Integer>>();
        for(String name : map.keySet()) {
            reportList.add(new Pair<String, Integer>(name, map.get(name)));
            if(map.get(name) > 0) {
                totalIncome += map.get(name);
            }
            else {
                totalExpense += map.get(name);
            }
        }

        //Add summary row to list
        reportList.add(new Pair<String, Integer>(
                MTApp.get().getResources().getString(R.string.total_incomes) + " :", totalIncome));
        reportList.add(new Pair<String, Integer>(
                MTApp.get().getResources().getString(R.string.total_expenses) + " :", totalExpense));
        reportList.add(new Pair<String, Integer>(
                MTApp.get().getResources().getString(R.string.total) + " :", totalExpense + totalIncome));
    }
}
