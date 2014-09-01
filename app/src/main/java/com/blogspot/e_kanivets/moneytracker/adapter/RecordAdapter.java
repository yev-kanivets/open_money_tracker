package com.blogspot.e_kanivets.moneytracker.adapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.helper.DBHelper;
import com.blogspot.e_kanivets.moneytracker.model.Record;
import com.blogspot.e_kanivets.moneytracker.util.Constants;

import java.util.List;

/**
 * Created by eugene on 01/09/14.
 */
public class RecordAdapter extends BaseAdapter{

    private Context context;
    private List<Record> records;

    private View.OnClickListener onClickListener;

    public RecordAdapter(Context context, List<Record> records, View.OnClickListener onClickListener) {
        this.context = context;
        this.records = records;
        this.onClickListener = onClickListener;
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

        TextView tvPrice = (TextView) convertView.findViewById(R.id.tv_price);
        TextView tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
        TextView tvCategory = (TextView) convertView.findViewById(R.id.tv_category);

        Button bRemove = (Button) convertView.findViewById(R.id.b_remove);

        tvPrice.setText(records.get(position).getPrice());
        tvTitle.setText(records.get(position).getTitle());
        tvCategory.setText(records.get(position).getCategory());

        bRemove.setTag(position);
        bRemove.setOnClickListener(onClickListener);

        return convertView;
    }
}
