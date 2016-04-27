package com.blogspot.e_kanivets.moneytracker.activity;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.activity.base.BaseBackActivity;
import com.blogspot.e_kanivets.moneytracker.report.chart.BarChartConverter;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;

import butterknife.Bind;

public class ChartsActivity extends BaseBackActivity {

    @Bind(R.id.bar_chart)
    BarChart barChart;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_charts;
    }

    @Override
    protected void initViews() {
        super.initViews();

        BarChartConverter barChartConverter = new BarChartConverter(ChartsActivity.this);

        BarData barData = new BarData(barChartConverter.getXAxisValueList(),
                barChartConverter.getBarDataSetList());
        barData.setDrawValues(false);

        barChart.setData(barData);
        barChart.setDescription(null);
        barChart.setVisibleXRangeMinimum(8);
        barChart.setScaleYEnabled(false);
        barChart.setVisibleXRangeMaximum(34);
    }
}
