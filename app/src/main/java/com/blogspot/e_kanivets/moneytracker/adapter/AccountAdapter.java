package com.blogspot.e_kanivets.moneytracker.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.entity.Account;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Custom adapter class for Account entity.
 * Created on 6/3/15.
 *
 * @author Evgenii Kanivets
 */
public class AccountAdapter extends BaseAdapter {
    private Context context;
    private List<Account> accounts;

    private int whiteRed;
    private int whiteGreen;
    private int red;
    private int green;

    @SuppressWarnings("deprecation")
    public AccountAdapter(Context context, List<Account> accounts) {
        this.context = context;
        this.accounts = accounts;

        whiteRed = context.getResources().getColor(R.color.white_red);
        whiteGreen = context.getResources().getColor(R.color.white_green);
        red = context.getResources().getColor(R.color.red);
        green = context.getResources().getColor(R.color.green);
    }

    @Override
    public int getCount() {
        return accounts.size();
    }

    @Override
    public Object getItem(int position) {
        return accounts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);

            convertView = layoutInflater.inflate(R.layout.view_account, parent, false);
            viewHolder = new ViewHolder(convertView);

            convertView.setTag(viewHolder);
        } else viewHolder = (ViewHolder) convertView.getTag();

        Account account = accounts.get(position);

        convertView.setBackgroundColor(account.getCurSum() >= 0 ? whiteGreen : whiteRed);

        viewHolder.tvCurSum.setTextColor(account.getCurSum() >= 0 ? green : red);
        viewHolder.tvCurrency.setTextColor(account.getCurSum() >= 0 ? green : red);

        viewHolder.tvTitle.setText(account.getTitle());
        viewHolder.tvCurSum.setText(Integer.toString(account.getCurSum()));
        viewHolder.tvCurrency.setText(account.getCurrency());

        return convertView;
    }

    public static class ViewHolder {
        @Bind(R.id.tv_title)
        TextView tvTitle;
        @Bind(R.id.tv_cur_sum)
        TextView tvCurSum;
        @Bind(R.id.tv_currency)
        TextView tvCurrency;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}