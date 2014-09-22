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

        //Sort reportList
        List<Pair<String, Integer>> reportIncomes = new ArrayList<Pair<String, Integer>>();
        List<Pair<String, Integer>> reportExpenses = new ArrayList<Pair<String, Integer>>();

        for(String name : map.keySet()) {
            if(map.get(name) > 0) {
                totalIncome += map.get(name);
                reportIncomes.add(new Pair<String, Integer>(name, map.get(name)));
            }
            else {
                totalExpense += map.get(name);
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
        reportList.add(new Pair<String, Integer>(
                MTApp.get().getResources().getString(R.string.total_incomes) + " :", totalIncome));
        reportList.add(new Pair<String, Integer>(
                MTApp.get().getResources().getString(R.string.total_expenses) + " :", totalExpense));
        reportList.add(new Pair<String, Integer>(
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
