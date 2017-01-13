package com.blogspot.e_kanivets.moneytracker.activity.charts;

import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.activity.base.BaseBackActivity;
import com.blogspot.e_kanivets.moneytracker.activity.charts.fragment.GraphFragment;
import com.blogspot.e_kanivets.moneytracker.activity.charts.fragment.SummaryFragment;
import com.blogspot.e_kanivets.moneytracker.controller.CurrencyController;
import com.blogspot.e_kanivets.moneytracker.controller.data.ExchangeRateController;
import com.blogspot.e_kanivets.moneytracker.controller.data.RecordController;
import com.blogspot.e_kanivets.moneytracker.entity.data.Record;
import com.blogspot.e_kanivets.moneytracker.report.ReportMaker;
import com.blogspot.e_kanivets.moneytracker.report.chart.IMonthReport;

import java.util.ArrayList;
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

    @Bind(R.id.tabs)
    TabLayout tabLayout;
    @Bind(R.id.view_pager)
    ViewPager viewPager;

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

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
    }

    protected String createRatesNeededList(String currency, List<String> ratesNeeded) {
        StringBuilder sb = new StringBuilder(getString(R.string.error_exchange_rates));

        for (String str : ratesNeeded) {
            sb.append("\n").append(str).append(getString(R.string.arrow)).append(currency);
        }

        return sb.toString();
    }

    private void setupViewPager(ViewPager viewPager) {
        ReportMaker reportMaker = new ReportMaker(exchangeRateController);
        String currency = currencyController.readDefaultCurrency();
        List<Record> recordList = recordController.readAll();
        List<String> currencyNeeded = reportMaker.currencyNeeded(currency, recordList);

        IMonthReport monthReport = null;
        if (currencyNeeded.isEmpty()) monthReport = reportMaker.getMonthReport(currency, recordList);

        Fragment graphFragment;
        if (monthReport == null) {
            graphFragment = GraphFragment.newInstance(createRatesNeededList(currency, currencyNeeded));
        } else {
            graphFragment = GraphFragment.newInstance(monthReport);
        }

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(SummaryFragment.newInstance(monthReport), "Summary");
        adapter.addFragment(graphFragment, "Graph");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
