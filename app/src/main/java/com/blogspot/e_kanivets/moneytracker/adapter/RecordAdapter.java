package com.blogspot.e_kanivets.moneytracker.adapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.helper.DBHelper;
import com.blogspot.e_kanivets.moneytracker.helper.MTHelper;
import com.blogspot.e_kanivets.moneytracker.model.Record;
import com.blogspot.e_kanivets.moneytracker.util.Constants;

import java.text.SimpleDateFormat;
import java.util.Date;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(R.layout.view_record, null);

        TextView tvTime = (TextView) convertView.findViewById(R.id.tv_date_and_time);
        TextView tvPrice = (TextView) convertView.findViewById(R.id.tv_price);
        TextView tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
        TextView tvCategory = (TextView) convertView.findViewById(R.id.tv_category);

        //Set background color of view according to type
        convertView.setBackgroundColor(records.get(position).isIncome() ?
                context.getResources().getColor(R.color.white_green) :
                context.getResources().getColor(R.color.white_red));

        //Set color of price
        tvPrice.setTextColor(records.get(position).isIncome() ?
                context.getResources().getColor(R.color.green) :
                context.getResources().getColor(R.color.red));

        //Format date of record to display it on screen
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        tvTime.setText(dateFormat.format(new Date(records.get(position).getTime())));

        tvPrice.setText((records.get(position).isIncome() ? "+ " : "- ")
                + "$" + Integer.toString(records.get(position).getPrice()));
        tvTitle.setText(records.get(position).getTitle());
        tvCategory.setText(records.get(position).getCategory());

        return convertView;
    }
}
