package com.blogspot.e_kanivets.moneytracker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.model.Account;

import java.util.List;

/**
 * Custom adapter class for Account entity
 * Created by evgenii on 6/3/15.
 */
public class AccountAdapter extends BaseAdapter {

    private Context context;
    private List<Account> accounts;

    public AccountAdapter(Context context, List<Account> accounts) {
        this.context = context;
        this.accounts = accounts;

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

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(R.layout.view_account, null);

        TextView tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
        TextView tvCurSum = (TextView) convertView.findViewById(R.id.tv_cur_sum);

        tvTitle.setText(accounts.get(position).getTitle());
        tvCurSum.setText(Integer.toString(accounts.get(position).getCurSum()));

        return convertView;
    }
}