package com.blogspot.e_kanivets.moneytracker.ui.presenter.base;

import android.content.Context;
import android.view.LayoutInflater;

import com.blogspot.e_kanivets.moneytracker.R;

import java.util.List;

/**
 * Base summary presenter to encapsulate some common methods.
 * Created on 4/6/16.
 */
public abstract class BaseSummaryPresenter {
    protected Context context;
    protected LayoutInflater layoutInflater;

    protected String createRatesNeededList(String currency, List<String> ratesNeeded) {
        StringBuilder sb = new StringBuilder(context.getString(R.string.error_exchange_rates));

        for (String str : ratesNeeded) {
            sb.append("\n").append(str).append(context.getString(R.string.arrow)).append(currency);
        }

        return sb.toString();
    }
}
