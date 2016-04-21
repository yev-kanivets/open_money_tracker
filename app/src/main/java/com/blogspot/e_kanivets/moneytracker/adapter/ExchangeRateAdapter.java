package com.blogspot.e_kanivets.moneytracker.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.entity.data.ExchangeRate;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Custom adapter class for Exchange rate entity.
 * Created on 23/2/16.
 *
 * @author Evgenii Kanivets
 */
public class ExchangeRateAdapter extends BaseAdapter {
    private Context context;
    private List<ExchangeRate> exchangeRates;

    public ExchangeRateAdapter(Context context, List<ExchangeRate> exchangeRates) {
        this.context = context;
        this.exchangeRates = exchangeRates;
    }

    @Override
    public int getCount() {
        return exchangeRates.size();
    }

    @Override
    public ExchangeRate getItem(int position) {
        return exchangeRates.get(position);
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

            convertView = layoutInflater.inflate(R.layout.view_exchange_rate, parent, false);
            viewHolder = new ViewHolder(convertView);

            convertView.setTag(viewHolder);
        } else viewHolder = (ViewHolder) convertView.getTag();

        ExchangeRate rate = getItem(position);

        viewHolder.tvFromCurrency.setText(rate.getFromCurrency());
        viewHolder.tvToCurrency.setText(rate.getToCurrency());
        viewHolder.tvAmount.setText(Double.toString(rate.getAmount()));

        return convertView;
    }

    public static class ViewHolder {
        @Bind(R.id.tv_from_currency)
        TextView tvFromCurrency;
        @Bind(R.id.tv_to_currency)
        TextView tvToCurrency;
        @Bind(R.id.tv_amount)
        TextView tvAmount;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}