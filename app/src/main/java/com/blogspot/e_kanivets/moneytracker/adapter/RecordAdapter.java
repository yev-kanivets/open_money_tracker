package com.blogspot.e_kanivets.moneytracker.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.blogspot.e_kanivets.moneytracker.MtApp;
import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.controller.FormatController;
import com.blogspot.e_kanivets.moneytracker.entity.data.Record;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Custom adapter class for {@link Record} entity.
 * Created on 01/09/14.
 *
 * @author Evgenii Kanivets
 */
public class RecordAdapter extends BaseAdapter {
    @Inject
    FormatController formatController;

    private Context context;
    private List<Record> records;

    private int whiteRed;
    private int whiteGreen;
    private int red;
    private int green;

    @SuppressWarnings("deprecation")
    public RecordAdapter(Context context, List<Record> records) {
        MtApp.get().getAppComponent().inject(RecordAdapter.this);

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
        viewHolder.container.setBackgroundColor(record.isIncome() ? whiteGreen : whiteRed);
        viewHolder.tvPrice.setTextColor(record.isIncome() ? green : red);

        viewHolder.tvDateAndTime.setText(formatController.formatDateAndTime(record.getTime()));
        viewHolder.tvPrice.setText(formatController.formatSignedAmount(
                (record.isIncome() ? 1 : -1) * record.getFullPrice()));
        viewHolder.tvTitle.setText(record.getTitle());
        if (record.getCategory() != null)
            viewHolder.tvCategory.setText(record.getCategory().getName());
        viewHolder.tvCurrency.setText(record.getCurrency());

        return convertView;
    }

    public static class ViewHolder {
        @BindView(R.id.container)
        View container;
        @BindView(R.id.tv_date_and_time)
        TextView tvDateAndTime;
        @BindView(R.id.tv_price)
        TextView tvPrice;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_category)
        TextView tvCategory;
        @BindView(R.id.tvCurrency)
        TextView tvCurrency;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
