package com.blogspot.e_kanivets.moneytracker.report;

import com.blogspot.e_kanivets.moneytracker.controller.ExchangeRateController;
import com.blogspot.e_kanivets.moneytracker.model.Period;
import com.blogspot.e_kanivets.moneytracker.model.Record;
import com.blogspot.e_kanivets.moneytracker.report.base.IExchangeRateProvider;
import com.blogspot.e_kanivets.moneytracker.report.base.IReport;

import java.util.List;

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

    public IReport getReport(String currency, Period period, List<Record> recordList) {
        IExchangeRateProvider rateProvider = new ExchangeRateProvider(currency, rateController);
        return new Report(currency, period, recordList, rateProvider);
    }
}