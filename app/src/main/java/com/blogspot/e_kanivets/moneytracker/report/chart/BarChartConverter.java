package com.blogspot.e_kanivets.moneytracker.report.chart;

import android.content.Context;
import android.support.annotation.NonNull;

import com.blogspot.e_kanivets.moneytracker.R;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Util class to convert {@link BarChartReport}
 * to {@link com.github.mikephil.charting.charts.BarChart} input data.
 * Created on 4/27/16.
 *
 * @author Evgenii Kanivets
 */
public class BarChartConverter {
    private final int green;
    private final int red;

    public BarChartConverter(Context context) {
        green = context.getResources().getColor(R.color.green_light);
        red = context.getResources().getColor(R.color.red_light);
    }

    @NonNull
    public List<String> getXAxisValueList() {
        List<String> valueList = new ArrayList<>();
        String[] xVals = {"Jan 16", "Feb 16", "Mar 16", "Apr 16", "May 16", "Jun 16", "Jul 16",
                "Aug 16", "Sep 16", "Oct 16", "Nov 16", "Dec 16", "Jan 17", "Feb 17", "Mar 17",
                "Apr 17", "May 17", "Jun 17", "Jul 17", "Aug 17", "Sep 17", "Oct 17", "Nov 17", "Dec 17"};
        valueList.addAll(Arrays.asList(xVals));
        return valueList;
    }

    @NonNull
    public List<IBarDataSet> getBarDataSetList() {
        List<BarEntry> list1 = new ArrayList<>();
        list1.add(new BarEntry(100, 0));
        list1.add(new BarEntry(110, 1));
        list1.add(new BarEntry(60, 2));
        list1.add(new BarEntry(90, 3));
        list1.add(new BarEntry(150, 4));
        list1.add(new BarEntry(50, 5));
        list1.add(new BarEntry(100, 6));
        list1.add(new BarEntry(110, 7));
        list1.add(new BarEntry(60, 8));
        list1.add(new BarEntry(90, 9));
        list1.add(new BarEntry(150, 10));
        list1.add(new BarEntry(50, 11));
        list1.add(new BarEntry(100, 12));
        list1.add(new BarEntry(110, 13));
        list1.add(new BarEntry(60, 14));
        list1.add(new BarEntry(90, 15));
        list1.add(new BarEntry(150, 16));
        list1.add(new BarEntry(50, 17));
        list1.add(new BarEntry(100, 18));
        list1.add(new BarEntry(110, 19));
        list1.add(new BarEntry(60, 20));
        list1.add(new BarEntry(90, 21));
        list1.add(new BarEntry(150, 22));
        list1.add(new BarEntry(50, 23));
        BarDataSet dataSet1 = new BarDataSet(list1, "Incomes");
        dataSet1.setColor(green);

        List<BarEntry> list2 = new ArrayList<>();
        list2.add(new BarEntry(50, 0));
        list2.add(new BarEntry(10, 1));
        list2.add(new BarEntry(30, 2));
        list2.add(new BarEntry(30, 3));
        list2.add(new BarEntry(50, 4));
        list2.add(new BarEntry(10, 5));
        list2.add(new BarEntry(50, 6));
        list2.add(new BarEntry(10, 7));
        list2.add(new BarEntry(30, 8));
        list2.add(new BarEntry(30, 9));
        list2.add(new BarEntry(50, 10));
        list2.add(new BarEntry(10, 11));
        list2.add(new BarEntry(100, 12));
        list2.add(new BarEntry(110, 13));
        list2.add(new BarEntry(60, 14));
        list2.add(new BarEntry(90, 15));
        list2.add(new BarEntry(150, 16));
        list2.add(new BarEntry(50, 17));
        list2.add(new BarEntry(100, 18));
        list2.add(new BarEntry(110, 19));
        list2.add(new BarEntry(60, 20));
        list2.add(new BarEntry(90, 21));
        list2.add(new BarEntry(150, 22));
        list2.add(new BarEntry(50, 23));
        BarDataSet dataSet2 = new BarDataSet(list2, "Expenses");
        dataSet2.setColor(red);

        List<IBarDataSet> list = new ArrayList<>();
        list.add(dataSet1);
        list.add(dataSet2);

        return list;
    }
}
