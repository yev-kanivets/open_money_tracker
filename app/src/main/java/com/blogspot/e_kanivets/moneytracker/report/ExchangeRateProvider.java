package com.blogspot.e_kanivets.moneytracker.report;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.blogspot.e_kanivets.moneytracker.controller.ExchangeRateController;
import com.blogspot.e_kanivets.moneytracker.entity.ExchangeRate;
import com.blogspot.e_kanivets.moneytracker.entity.Record;
import com.blogspot.e_kanivets.moneytracker.report.base.IExchangeRateProvider;

import java.util.Map;
import java.util.TreeMap;

/**
 * First {@link IExchangeRateProvider} implementation.
 * Created on 2/25/16.
 *
 * @author Evgenii Kanivets
 */
public class ExchangeRateProvider implements IExchangeRateProvider {
    @SuppressWarnings("unused")
    private static final String TAG = "ExchangeRateProvider";

    private String toCurrency;
    private final ExchangeRateController controller;
    private final Map<String, ExchangeRate> rateMap;

    /**
     * @param toCurrency code of toCurrency to convert to
     * @param controller dependency to get needed rates
     */
    public ExchangeRateProvider(String toCurrency, ExchangeRateController controller) {
        this.toCurrency = toCurrency;
        this.controller = controller;

        rateMap = getRateMap();
    }

    @Nullable
    @Override
    public ExchangeRate getRate(@Nullable Record record) {
        if (record == null) return null;

        String fromCurrency = record.getCurrency();

        return rateMap.get(fromCurrency);
    }

    @NonNull
    private Map<String, ExchangeRate> getRateMap() {
        Map<String, ExchangeRate> rateMap = new TreeMap<>();

        for (ExchangeRate rate : controller.readAll()) {
            if (!toCurrency.equals(rate.getToCurrency())) continue;
            if (rateMap.containsKey(rate.getFromCurrency())) continue;

            rateMap.put(rate.getFromCurrency(), rate);
        }

        return rateMap;
    }
}