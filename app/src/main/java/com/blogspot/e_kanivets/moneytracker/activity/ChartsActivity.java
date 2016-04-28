package com.blogspot.e_kanivets.moneytracker.activity;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.activity.base.BaseBackActivity;
import com.blogspot.e_kanivets.moneytracker.controller.CurrencyController;
import com.blogspot.e_kanivets.moneytracker.controller.data.ExchangeRateController;
import com.blogspot.e_kanivets.moneytracker.controller.data.RecordController;
import com.blogspot.e_kanivets.moneytracker.entity.data.Record;
import com.blogspot.e_kanivets.moneytracker.report.ReportMaker;
import com.blogspot.e_kanivets.moneytracker.report.chart.BarChartConverter;
import com.blogspot.e_kanivets.moneytracker.report.chart.IMonthReport;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;

public class ChartsActivity extends BaseBackActivity {

    @Inject
    RecordController recordController;
    @Inject
    ExchangeRateController exchangeRateController;
    @Inject
    CurrencyController currencyController;

    @Bind(R.id.bar_chart)
    BarChart barChart;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_charts;
    }

    @Override
    protected boolean initData() {
        boolean result = super.initData();
        getAppComponent().inject(ChartsActivity.this);
        return result;
    }

    @Override
    protected void initViews() {
        super.initViews();

        ReportMaker reportMaker = new ReportMaker(exchangeRateController);
        String currency = currencyController.readDefaultCurrency();
        List<Record> recordList = recordController.readAll();
        List<String> currencyNeeded = reportMaker.currencyNeeded(currency, recordList);

        IMonthReport monthReport = null;
        if (currencyNeeded.isEmpty()) monthReport = reportMaker.getMonthReport(currency, recordList);
        else barChart.setNoDataText(createRatesNeededList(currency, currencyNeeded));

        if (monthReport != null) {
            BarChartConverter barChartConverter = new BarChartConverter(ChartsActivity.this,
                    monthReport);

            BarData barData = new BarData(barChartConverter.getXAxisValueList(),
                    barChartConverter.getBarDataSetList());
            barData.setDrawValues(false);

            barChart.setData(barData);
            barChart.setDescription(null);
            barChart.setVisibleXRangeMinimum(8);
            barChart.setScaleYEnabled(false);
            barChart.setVisibleXRangeMaximum(34);
            barChart.setHighlightPerDragEnabled(false);
            barChart.setHighlightPerTapEnabled(false);
        }
    }

    protected String createRatesNeededList(String currency, List<String> ratesNeeded) {
        StringBuilder sb = new StringBuilder(getString(R.string.error_exchange_rates));

        for (String str : ratesNeeded) {
            sb.append("\n").append(str).append(getString(R.string.arrow)).append(currency);
        }

        return sb.toString();
    }
}
