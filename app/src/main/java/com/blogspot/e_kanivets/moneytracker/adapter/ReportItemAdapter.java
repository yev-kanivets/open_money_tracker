package com.blogspot.e_kanivets.moneytracker.adapter;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.blogspot.e_kanivets.moneytracker.R;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by eugene on 11/09/14.
 */
public class ReportItemAdapter extends BaseAdapter {

    Context context;
    List<Pair<String, Integer>> records;

    public ReportItemAdapter(Context context, List<Pair<String, Integer>> records) {
        this.context = context;
        this.records = records;
    }

    @Override
    public int getCount() {
        return records.size();
    }

    @Override
    public Object getItem(int i) {
        return records.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.view_report_item, null);

        //Customize view to fit to model and UI
        if(i == getCount()-1) {
            view.findViewById(R.id.line).setVisibility(View.VISIBLE);
        }
        view.setBackgroundColor(records.get(i).second < 0 ? context.getResources().getColor(R.color.white_red) :
                context.getResources().getColor(R.color.white_green));

        TextView tvCategory = (TextView) view.findViewById(R.id.tv_category);
        TextView tvTotal = (TextView) view.findViewById(R.id.tv_total);

        //Set color of total
        tvTotal.setTextColor(records.get(i).second >= 0 ?
                context.getResources().getColor(R.color.green) :
                context.getResources().getColor(R.color.red));

        tvCategory.setText(records.get(i).first);
        tvTotal.setText((records.get(i).second >= 0 ? "+ " : "- ") + Math.abs(records.get(i).second));

        return view;
    }
}
