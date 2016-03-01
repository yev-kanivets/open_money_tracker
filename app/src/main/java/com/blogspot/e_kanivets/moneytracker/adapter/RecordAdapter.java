package com.blogspot.e_kanivets.moneytracker.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.entity.Record;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Custom adapter class for {@link Record} entity.
 * Created on 01/09/14.
 *
 * @author Evgenii Kanivets
 */
public class RecordAdapter extends BaseAdapter{
    private Context context;
    private List<Record> records;

    private int whiteRed;
    private int whiteGreen;
    private int red;
    private int green;

    @SuppressWarnings("deprecation")
    public RecordAdapter(Context context, List<Record> records) {
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
    public Record getItem(int position) {
        return records.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);

            convertView = layoutInflater.inflate(R.layout.view_record, parent, false);
            viewHolder = new ViewHolder(convertView);

            convertView.setTag(viewHolder);
        } else viewHolder = (ViewHolder) convertView.getTag();

        Record record = getItem(position);

        convertView.setBackgroundColor(record.isIncome() ? whiteGreen : whiteRed);
        viewHolder.tvPrice.setTextColor(record.isIncome() ? green : red);

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        viewHolder.tvDateAndTime.setText(dateFormat.format(new Date(record.getTime())));

        viewHolder.tvPrice.setText((record.isIncome() ? "+ " : "- ")
                + Integer.toString(record.getPrice()));
        viewHolder.tvTitle.setText(record.getTitle());
        viewHolder.tvCategory.setText(record.getCategory().getName());
        viewHolder.tvCurrency.setText(record.getCurrency());

        return convertView;
    }

    public static class ViewHolder {
        @Bind(R.id.tv_date_and_time)
        TextView tvDateAndTime;
        @Bind(R.id.tv_price)
        TextView tvPrice;
        @Bind(R.id.tv_title)
        TextView tvTitle;
        @Bind(R.id.tv_category)
        TextView tvCategory;
        @Bind(R.id.tv_currency)
        TextView tvCurrency;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}