package com.blogspot.e_kanivets.moneytracker.ui.presenter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.report.base.IReport;
import com.blogspot.e_kanivets.moneytracker.ui.presenter.base.BaseSummaryPresenter;

import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Util class to create and manage summary header view for .
 * Created on 2/26/16.
 *
 * @author Evgenii Kanivets
 */
public class ShortSummaryPresenter extends BaseSummaryPresenter {

    private int red;
    private int green;
    private View view;

    @SuppressWarnings("deprecation")
    public ShortSummaryPresenter(Context context) {
        this.context = context;

        layoutInflater = LayoutInflater.from(context);
        red = context.getResources().getColor(R.color.red);
        green = context.getResources().getColor(R.color.green);
    }

    public View create(boolean shortSummary) {
        view = layoutInflater.inflate(R.layout.view_summary_records, null);
        view.findViewById(R.id.iv_more).setVisibility(shortSummary ? View.VISIBLE : View.INVISIBLE);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    public void update(IReport report, String currency, List<String> ratesNeeded) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        if (report == null) {
            viewHolder.tvTotalIncome.setText("");
            viewHolder.tvTotalExpense.setText("");

            viewHolder.tvTotal.setTextColor(red);
            viewHolder.tvTotal.setText(createRatesNeededList(currency, ratesNeeded));
        } else {
            viewHolder.tvPeriod.setText(report.getPeriod().getFirstDay() + " - " + report.getPeriod().getLastDay());

            viewHolder.tvTotalIncome.setTextColor(report.getTotalIncome() >= 0 ? green : red);
            viewHolder.tvTotalIncome.setText(format(report.getTotalIncome(), report.getCurrency()));

            viewHolder.tvTotalExpense.setTextColor(report.getTotalExpense() >= 0 ? green : red);
            viewHolder.tvTotalExpense.setText(format(report.getTotalExpense(), report.getCurrency()));

            viewHolder.tvTotal.setTextColor(report.getTotal() >= 0 ? green : red);
            viewHolder.tvTotal.setText(format(report.getTotal(), report.getCurrency()));
        }
    }

    private String format(double amount, String currency) {
        return (amount >= 0 ? "+ " : "- ") + String.format(Locale.getDefault(), "%.0f", Math.abs(amount))
                + " " + currency;
    }

    public static class ViewHolder {
        @Bind(R.id.tv_period)
        TextView tvPeriod;
        @Bind(R.id.tv_total_income)
        TextView tvTotalIncome;
        @Bind(R.id.tv_total_expense)
        TextView tvTotalExpense;
        @Bind(R.id.tv_total)
        TextView tvTotal;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}