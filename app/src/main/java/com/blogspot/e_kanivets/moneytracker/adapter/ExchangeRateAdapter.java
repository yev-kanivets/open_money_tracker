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
import com.blogspot.e_kanivets.moneytracker.entity.ExchangeRatePair;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Custom adapter class for Exchange rate entity.
 * Created on 23/2/16.
 *
 * @author Evgenii Kanivets
 */
public class ExchangeRateAdapter extends BaseAdapter {
    @Inject
    FormatController formatController;

    private Context context;
    private List<ExchangeRatePair> exchangeRates;

    public ExchangeRateAdapter(Context context, List<ExchangeRatePair> exchangeRates) {
        this.context = context;
        this.exchangeRates = exchangeRates;
        MtApp.get().getAppComponent().inject(ExchangeRateAdapter.this);
    }

    @Override
    public int getCount() {
        return exchangeRates.size();
    }

    @Override
    public ExchangeRatePair getItem(int position) {
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

        ExchangeRatePair rate = getItem(position);

        viewHolder.tvFromCurrency.setText(rate.getFromCurrency());
        viewHolder.tvToCurrency.setText(rate.getToCurrency());
        viewHolder.tvAmountBuy.setText(formatController.formatPrecisionNone(rate.getAmountBuy()));
        viewHolder.tvAmountSell.setText(formatController.formatPrecisionNone(rate.getAmountSell()));

        return convertView;
    }

    public static class ViewHolder {
        @Bind(R.id.tv_from_currency)
        TextView tvFromCurrency;
        @Bind(R.id.tv_to_currency)
        TextView tvToCurrency;
        @Bind(R.id.tv_amount_buy)
        TextView tvAmountBuy;
        @Bind(R.id.tv_amount_sell)
        TextView tvAmountSell;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
