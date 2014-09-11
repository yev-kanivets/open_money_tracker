package com.blogspot.e_kanivets.moneytracker.model;

import android.util.Pair;

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

        //Convert HashMap to ArrayList
        reportList = new ArrayList<Pair<String, Integer>>();
        for(String name : map.keySet()) {
            reportList.add(new Pair<String, Integer>(name, map.get(name)));
        }
    }
}
