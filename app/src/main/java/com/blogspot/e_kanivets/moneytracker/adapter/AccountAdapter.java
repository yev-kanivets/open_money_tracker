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

        viewHolder.tvTitle.setText(account.getTitle());
        viewHolder.tvCurSum.setText(Integer.toString(account.getCurSum())
                + " " + account.getCurrency());

        return convertView;
    }

    public static class ViewHolder {
        @Bind(R.id.tv_title)
        TextView tvTitle;
        @Bind(R.id.tv_cur_sum)
        TextView tvCurSum;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}