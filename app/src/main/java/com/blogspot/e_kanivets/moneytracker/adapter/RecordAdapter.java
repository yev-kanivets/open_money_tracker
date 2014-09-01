package com.blogspot.e_kanivets.moneytracker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.model.Record;

import java.util.List;

/**
 * Created by eugene on 01/09/14.
 */
public class RecordAdapter extends BaseAdapter{

    private Context context;
    private List<Record> records;

    public RecordAdapter(Context context, List<Record> records) {
        this.context = context;
        this.records = records;
    }

    @Override
    public int getCount() {
        return records.size();
    }

    @Override
    public Object getItem(int position) {
        return records.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(R.layout.view_record, parent);

        TextView tvPrice = (TextView) convertView.findViewById(R.id.tv_price);
        TextView tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
        TextView tvCategory = (TextView) convertView.findViewById(R.id.tv_category);

        tvPrice.setText(records.get(position).getPrice());
        tvTitle.setText(records.get(position).getTitle());
        tvCategory.setText(records.get(position).getCategory());

        return convertView;
    }
}
