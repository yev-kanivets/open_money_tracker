package com.blogspot.e_kanivets.moneytracker.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.report.base.IReport;

import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Util class to encapsulate Total view creation.
 * Created on 2/26/16.
 *
 * @author Evgenii Kanivets
 */
public class TotalReportViewCreator {
    private final IReport report;
    private final LayoutInflater layoutInflater;

    private int whiteRed;
    private int whiteGreen;
    private int red;
    private int green;

    @SuppressWarnings("deprecation")
    public TotalReportViewCreator(Context context, IReport report) {
        this.report = report;
        layoutInflater = LayoutInflater.from(context);

        whiteRed = context.getResources().getColor(R.color.white_red);
        whiteGreen = context.getResources().getColor(R.color.white_green);
        red = context.getResources().getColor(R.color.red);
        green = context.getResources().getColor(R.color.green);
    }

    public View create() {
        View view = layoutInflater.inflate(R.layout.view_summary_report, null);

        ViewHolder viewHolder = new ViewHolder(view);

        viewHolder.llTotalIncome.setBackgroundColor(report.getTotalIncome() < 0 ? whiteRed : whiteGreen);
        viewHolder.tvTotalIncome.setTextColor(report.getTotalIncome() >= 0 ? green : red);
        viewHolder.tvTotalIncome.setText(format(report.getTotalIncome()));

        viewHolder.llTotalExpense.setBackgroundColor(report.getTotalExpense() < 0 ? whiteRed : whiteGreen);
        viewHolder.tvTotalExpense.setTextColor(report.getTotalExpense() >= 0 ? green : red);
        viewHolder.tvTotalExpense.setText(format(report.getTotalExpense()));

        viewHolder.llTotal.setBackgroundColor(report.getTotal() < 0 ? whiteRed : whiteGreen);
        viewHolder.tvTotal.setTextColor(report.getTotal() >= 0 ? green : red);
        viewHolder.tvTotal.setText(format(report.getTotal()));

        return view;
    }

    private String format(double amount) {
        return (amount >= 0 ? "+ " : "- ") + String.format(Locale.getDefault(), "%.0f", Math.abs(amount));
    }

    public static class ViewHolder {
        @Bind(R.id.ll_total_income)
        View llTotalIncome;
        @Bind(R.id.tv_total_income)
        TextView tvTotalIncome;

        @Bind(R.id.ll_total_expense)
        View llTotalExpense;
        @Bind(R.id.tv_total_expense)
        TextView tvTotalExpense;

        @Bind(R.id.ll_total)
        View llTotal;
        @Bind(R.id.tv_total)
        TextView tvTotal;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}