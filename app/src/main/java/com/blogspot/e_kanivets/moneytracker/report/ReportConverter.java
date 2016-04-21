package com.blogspot.e_kanivets.moneytracker.report;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.report.base.IReport;
import com.blogspot.e_kanivets.moneytracker.report.model.CategoryRecord;
import com.blogspot.e_kanivets.moneytracker.report.model.SummaryRecord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Util class to convert {@link Report} to {@link android.widget.ExpandableListView} input data.
 * Created on 2/26/16.
 *
 * @author Evgenii Kanivets
 */
public class ReportConverter {
    public static final String TITLE_PARAM_NAME = "title";
    public static final String PRICE_PARAM_NAME = "price";

    private final IReport report;

    public ReportConverter(@NonNull IReport report) {
        this.report = report;
    }

    public List<? extends Map<String, String>> getGroupData() {
        List<Map<String, String>> groupData = new ArrayList<>();

        for (CategoryRecord categoryRecord : report.getSummary()) {
            Map<String, String> m = new HashMap<>();
            m.put(TITLE_PARAM_NAME, categoryRecord.getTitle());
            m.put(PRICE_PARAM_NAME, Double.toString(categoryRecord.getAmount()));

            groupData.add(m);
        }

        return groupData;
    }

    @LayoutRes
    public int getGroupLayout() {
        return R.layout.view_report_item_exp;
    }

    @NonNull
    public String[] getGroupFrom() {
        return new String[]{TITLE_PARAM_NAME, PRICE_PARAM_NAME};
    }

    @NonNull
    public int[] getGroupTo() {
        return new int[]{R.id.tv_category, R.id.tv_total};
    }

    @NonNull
    public List<? extends List<? extends Map<String, String>>> getChildData() {
        List<List<Map<String, String>>> childData = new ArrayList<>();

        for (CategoryRecord categoryRecord : report.getSummary()) {
            List<Map<String, String>> childDataItem = new ArrayList<>();
            for (SummaryRecord summaryRecord : categoryRecord.getSummaryRecordList()) {
                Map<String, String> m = new HashMap<>();
                m.put(TITLE_PARAM_NAME, summaryRecord.getTitle());
                m.put(PRICE_PARAM_NAME, Double.toString(summaryRecord.getAmount()));

                childDataItem.add(m);
            }

            childData.add(childDataItem);
        }

        return childData;
    }

    @LayoutRes
    public int getChildLayout() {
        return R.layout.view_report_item;
    }

    @NonNull
    public String[] getChildFrom() {
        return new String[]{TITLE_PARAM_NAME, PRICE_PARAM_NAME};
    }

    @NonNull
    public int[] getChildTo() {
        return new int[]{R.id.tv_category, R.id.tv_total};
    }
}