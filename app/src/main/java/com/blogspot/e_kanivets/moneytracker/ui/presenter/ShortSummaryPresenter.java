package com.blogspot.e_kanivets.moneytracker.ui.presenter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.blogspot.e_kanivets.moneytracker.MtApp;
import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.adapter.RecordAdapter;
import com.blogspot.e_kanivets.moneytracker.controller.FormatController;
import com.blogspot.e_kanivets.moneytracker.entity.Period;
import com.blogspot.e_kanivets.moneytracker.report.record.IRecordReport;
import com.blogspot.e_kanivets.moneytracker.ui.presenter.base.BaseSummaryPresenter;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Util class to create and manage summary header view for .
 * Created on 2/26/16.
 *
 * @author Evgenii Kanivets
 */
public class ShortSummaryPresenter extends BaseSummaryPresenter {

    @Inject
    FormatController formatController;

    private int red;
    private int green;
    private View view;

    @SuppressWarnings("deprecation")
    public ShortSummaryPresenter(Context context) {
        this.context = context;
        MtApp.get().getAppComponent().inject(ShortSummaryPresenter.this);

        layoutInflater = LayoutInflater.from(context);
        red = context.getResources().getColor(R.color.red);
        green = context.getResources().getColor(R.color.green);
    }

    public View create(boolean shortSummary, RecordAdapter.SummaryViewHolder viewHolder) {
        view = layoutInflater.inflate(R.layout.view_summary_records, null);
        view.findViewById(R.id.iv_more).setVisibility(shortSummary ? View.VISIBLE : View.INVISIBLE);
        view.setEnabled(false);
        view.findViewById(R.id.lvSummary).setClickable(false);
        view.findViewById(R.id.cvSummary).setClickable(true);
        view.setTag(viewHolder != null ? viewHolder : new ViewHolder(view));

        return view;
    }

    public void update(IRecordReport report, String currency, List<String> ratesNeeded) {
        SummaryViewInterface viewHolder = (SummaryViewInterface) view.getTag();
        if (report == null) {
            viewHolder.getTvTotalIncome().setText("");
            viewHolder.getTvTotalExpense().setText("");

            viewHolder.getTvTotal().setTextColor(red);
            viewHolder.getTvTotal().setText(createRatesNeededList(currency, ratesNeeded));
        } else {
            viewHolder.getTvPeriod().setText(formatPeriod(report.getPeriod()));

            viewHolder.getTvTotalIncome().setTextColor(report.getTotalIncome() >= 0 ? green : red);
            viewHolder.getTvTotalIncome().setText(formatController.formatIncome(report.getTotalIncome(),
                    report.getCurrency()));

            viewHolder.getTvTotalExpense().setTextColor(report.getTotalExpense() > 0 ? green : red);
            viewHolder.getTvTotalExpense().setText(formatController.formatExpense(report.getTotalExpense(),
                    report.getCurrency()));

            viewHolder.getTvTotal().setTextColor(report.getTotal() >= 0 ? green : red);
            viewHolder.getTvTotal().setText(formatController.formatIncome(report.getTotal(),
                    report.getCurrency()));
        }

    }

    private String formatPeriod(Period period) {
        switch (period.getType()) {
            case Period.TYPE_DAY:
                return period.getFirstDay();

            case Period.TYPE_MONTH:
                return new SimpleDateFormat("MMMM, yyyy").format(period.getFirst());

            case Period.TYPE_YEAR:
                return new SimpleDateFormat("yyyy").format(period.getFirst());

            case Period.TYPE_ALL_TIME:
                return context.getString(R.string.all_time);

            default:
                return context.getString(R.string.period_from_to, period.getFirstDay(),
                        period.getLastDay());
        }
    }

    public static class ViewHolder implements SummaryViewInterface {

        @BindView(R.id.tvPeriod)
        TextView tvPeriod;

        @Override
        public TextView getTvPeriod() {
            return tvPeriod;
        }

        @BindView(R.id.tvTotalIncome)
        TextView tvTotalIncome;

        @Override
        public TextView getTvTotalIncome() {
            return tvTotalIncome;
        }

        @BindView(R.id.tvTotalExpense)
        TextView tvTotalExpense;

        @Override
        public TextView getTvTotalExpense() {
            return tvTotalExpense;
        }

        @BindView(R.id.tvTotal)
        TextView tvTotal;

        @Override
        public TextView getTvTotal() {
            return tvTotal;
        }

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public interface SummaryViewInterface {
        TextView getTvPeriod();

        TextView getTvTotalIncome();

        TextView getTvTotalExpense();

        TextView getTvTotal();
    }

}