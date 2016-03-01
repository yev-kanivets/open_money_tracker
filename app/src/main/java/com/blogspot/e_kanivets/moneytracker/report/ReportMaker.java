package com.blogspot.e_kanivets.moneytracker.report;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.blogspot.e_kanivets.moneytracker.controller.ExchangeRateController;
import com.blogspot.e_kanivets.moneytracker.model.ExchangeRate;
import com.blogspot.e_kanivets.moneytracker.model.Period;
import com.blogspot.e_kanivets.moneytracker.model.Record;
import com.blogspot.e_kanivets.moneytracker.report.base.IExchangeRateProvider;
import com.blogspot.e_kanivets.moneytracker.report.base.IReport;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Util class to encapsulate {@link Report} generation logic.
 * Created on 2/26/16.
 *
 * @author Evgenii Kanivets
 */
public class ReportMaker {
    private final ExchangeRateController rateController;

    public ReportMaker(ExchangeRateController exchangeRateController) {
        this.rateController = exchangeRateController;
    }

    @Nullable
    public IReport getReport(String currency, Period period, List<Record> recordList) {
        if (currencyNeeded(currency, recordList).size() != 0) return null;

        IExchangeRateProvider rateProvider = new ExchangeRateProvider(currency, rateController);
        return new Report(currency, period, recordList, rateProvider);
    }

    @NonNull
    public List<String> currencyNeeded(String currency, List<Record> recordList) {
        Set<String> currencies = new TreeSet<>();

        for (Record record : recordList) {
            currencies.add(record.getCurrency());
        }

        currencies.remove(currency);

        for (ExchangeRate rate : rateController.readAll()) {
            if (rate.getToCurrency().equals(currency)) currencies.remove(rate.getFromCurrency());
        }

        return new ArrayList<>(currencies);
    }
}