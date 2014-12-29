package com.blogspot.e_kanivets.moneytracker.adapter;

import android.content.Context;
import android.widget.SimpleExpandableListAdapter;

import java.util.List;
import java.util.Map;

/**
 * Created by evgenii on 12/29/14.
 */
public class ExpandableListReportAdapter extends SimpleExpandableListAdapter {

    public ExpandableListReportAdapter(Context context, List<? extends Map<String, ?>> groupData,
                                       int groupLayout, String[] groupFrom, int[] groupTo,
                                       List<? extends List<? extends Map<String, ?>>> childData,
                                       int childLayout, String[] childFrom, int[] childTo) {
        super(context, groupData, groupLayout, groupFrom, groupTo, childData, childLayout, childFrom, childTo);
    }


}
