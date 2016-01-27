package com.blogspot.e_kanivets.moneytracker.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.model.Record;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Custom adapter class for {@link Record} entity.
 * Created on 11/09/14.
 *
 * @author Evgenii Kanivets
 */
public class ReportItemAdapter extends BaseAdapter {
    private Context context;
    private List<Pair<String, Integer>> records;

    private int whiteRed;
    private int whiteGreen;
    private int red;
    private int green;

    @SuppressWarnings("deprecation")
    public ReportItemAdapter(Context context, List<Pair<String, Integer>> records) {
        this.context = context;
        this.records = records;

        whiteRed = context.getResources().getColor(R.color.white_red);
        whiteGreen = context.getResources().getColor(R.color.white_green);
        red = context.getResources().getColor(R.color.red);
        green = context.getResources().getColor(R.color.green);
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

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);

            convertView = layoutInflater.inflate(R.layout.view_summary_report_item, parent, false);
            viewHolder = new ViewHolder(convertView);

            convertView.setTag(viewHolder);
        } else viewHolder = (ViewHolder) convertView.getTag();

        if (i == getCount() - 1) viewHolder.line.setVisibility(View.VISIBLE);
        else if (i == getCount() - 3) viewHolder.line.setVisibility(View.VISIBLE);

        convertView.setBackgroundColor(records.get(i).second < 0 ? whiteRed : whiteGreen);

        viewHolder.tvTotal.setTextColor(records.get(i).second >= 0 ? green : red);

        viewHolder.tvCategory.setText(records.get(i).first);
        viewHolder.tvTotal.setText((records.get(i).second >= 0 ? "+ " : "- ")
                + Math.abs(records.get(i).second));

        return convertView;
    }

    public static class ViewHolder {
        @Bind(R.id.line)
        View line;
        @Bind(R.id.tv_category)
        TextView tvCategory;
        @Bind(R.id.tv_total)
        TextView tvTotal;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}