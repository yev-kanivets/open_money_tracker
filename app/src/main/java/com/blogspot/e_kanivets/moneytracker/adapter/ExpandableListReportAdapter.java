package com.blogspot.e_kanivets.moneytracker.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.util.Constants;

import java.util.List;
import java.util.Map;

/**
 * Created by evgenii on 12/29/14.
 */
public class ExpandableListReportAdapter extends SimpleExpandableListAdapter {

    private static final String TAG = ExpandableListReportAdapter.class.getSimpleName();

    private Context context;
    private List<? extends Map<String, ?>> groupData;
    private List<? extends List<? extends Map<String, ?>>> childData;

    public ExpandableListReportAdapter(Context context, List<? extends Map<String, ?>> groupData,
                                       int groupLayout, String[] groupFrom, int[] groupTo,
                                       List<? extends List<? extends Map<String, ?>>> childData,
                                       int childLayout, String[] childFrom, int[] childTo) {
        super(context, groupData, groupLayout, groupFrom, groupTo, childData, childLayout, childFrom, childTo);

        this.context = context;
        this.groupData = groupData;
        this.childData = childData;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View view = super.getGroupView(groupPosition, isExpanded, convertView, parent);

        customizeView(view, (Map<String, String>) groupData.get(groupPosition), true);

        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View view = super.getChildView(groupPosition, childPosition, isLastChild, convertView, parent);
        customizeView(view, (Map<String, String>) childData.get(groupPosition).get(childPosition), false);
        return view;
    }

    private void customizeView(View view, Map<String, String> values, boolean groupView) {
        /* Customize view to fit to model and UI */
        Integer price = Integer.parseInt(values.get(Constants.PRICE_PARAM_NAME));

        if (groupView) {
            view.setBackgroundColor(price < 0 ? context.getResources().getColor(R.color.white_red) :
                    context.getResources().getColor(R.color.white_green));
        } else {
            view.setBackgroundColor(context.getResources().getColor(R.color.white));
        }

        TextView tvCategory = (TextView) view.findViewById(R.id.tv_category);
        TextView tvTotal = (TextView) view.findViewById(R.id.tv_total);

        //Set color of total
        tvTotal.setTextColor(price >= 0 ?
                context.getResources().getColor(R.color.green) :
                context.getResources().getColor(R.color.red));

        tvCategory.setText(values.get(Constants.TITLE_PARAM_NAME));
        tvTotal.setText((price >= 0 ? "+ " : "- ") + Math.abs(price));
    }
}
