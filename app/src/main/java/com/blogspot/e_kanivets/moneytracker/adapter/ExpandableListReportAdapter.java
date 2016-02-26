package com.blogspot.e_kanivets.moneytracker.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.report.ReportConverter;
import com.blogspot.e_kanivets.moneytracker.util.Constants;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created on 12/29/14.
 *
 * @author Evgenii Kanivets
 */
public class ExpandableListReportAdapter extends SimpleExpandableListAdapter {
    private List<? extends Map<String, String>> groupData;
    private List<? extends List<? extends Map<String, String>>> childData;

    private int whiteRed;
    private int whiteGreen;
    private int white;
    private int red;
    private int green;

    public ExpandableListReportAdapter(Context context, ReportConverter converter) {
        this(context, converter.getGroupData(), converter.getGroupLayout(),
                converter.getGroupFrom(), converter.getGroupTo(), converter.getChildData(),
                converter.getChildLayout(), converter.getChildFrom(), converter.getChildTo());
    }

    @SuppressWarnings("deprecation")
    public ExpandableListReportAdapter(Context context, List<? extends Map<String, String>> groupData,
                                       int groupLayout, String[] groupFrom, int[] groupTo,
                                       List<? extends List<? extends Map<String, String>>> childData,
                                       int childLayout, String[] childFrom, int[] childTo) {
        super(context, groupData, groupLayout, groupFrom, groupTo, childData, childLayout, childFrom, childTo);

        this.groupData = groupData;
        this.childData = childData;

        whiteRed = context.getResources().getColor(R.color.white_red);
        whiteGreen = context.getResources().getColor(R.color.white_green);
        white = context.getResources().getColor(R.color.white);
        red = context.getResources().getColor(R.color.red);
        green = context.getResources().getColor(R.color.green);
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View view = super.getGroupView(groupPosition, isExpanded, convertView, parent);
        customizeView(view, groupData.get(groupPosition), true);
        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View view = super.getChildView(groupPosition, childPosition, isLastChild, convertView, parent);
        customizeView(view, childData.get(groupPosition).get(childPosition), false);
        return view;
    }

    @SuppressLint("SetTextI18n")
    private void customizeView(View view, Map<String, String> values, boolean groupView) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        if (viewHolder == null) viewHolder = new ViewHolder(view);

        /* Customize view to fit to model and UI */
        Double price = Double.parseDouble(values.get(Constants.PRICE_PARAM_NAME));

        if (groupView) view.setBackgroundColor(price < 0 ? whiteRed : whiteGreen);
        else view.setBackgroundColor(white);

        //Set color of total
        viewHolder.tvTotal.setTextColor(price >= 0 ? green : red);

        viewHolder.tvCategory.setText(values.get(Constants.TITLE_PARAM_NAME));
        viewHolder.tvTotal.setText(format(price));
    }

    private String format(double amount) {
        return (amount >= 0 ? "+ " : "- ") + String.format(Locale.getDefault(), "%.0f", Math.abs(amount));
    }

    public static class ViewHolder {
        @Bind(R.id.tv_category)
        TextView tvCategory;
        @Bind(R.id.tv_total)
        TextView tvTotal;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}