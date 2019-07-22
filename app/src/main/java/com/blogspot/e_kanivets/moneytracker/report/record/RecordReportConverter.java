package com.blogspot.e_kanivets.moneytracker.report.record;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.report.record.model.CategoryRecord;
import com.blogspot.e_kanivets.moneytracker.report.record.model.SummaryRecord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Util class to convert {@link RecordReport} to {@link android.widget.ExpandableListView} input data.
 * Created on 2/26/16.
 *
 * @author Evgenii Kanivets
 */
public class RecordReportConverter {
    public static final String TITLE_PARAM_NAME = "title";
    public static final String PRICE_PARAM_NAME = "price";

    private final IRecordReport report;

    public RecordReportConverter(@NonNull IRecordReport report) {
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
        return new int[]{R.id.tvCategory, R.id.tvTotal};
    }

    @NonNull
    public List<? extends List<? extends Map<String, String>>> getChildData() {
        List<List<Map<String, String>>> childData = new ArrayList<>();

        for (CategoryRecord categoryRecord : report.getSummary()) {
            List<Map<String, String>> childDataItem = new ArrayList<>();
            for (SummaryRecord summaryRecord : categoryRecord.getSummaryRecordList()) {
                Map<String, String> m = new HashMap<>();
                m.put(TITLE_PARAM_NAME, getTitle(summaryRecord));
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
        return new int[]{R.id.tvCategory, R.id.tvTotal};
    }

    private String getTitle(@NonNull SummaryRecord record) {
        int count = record.getRecordList().size();
        if (count <= 1) return record.getTitle();
        else return record.getTitle() + " (" + count + ")";
    }
}
