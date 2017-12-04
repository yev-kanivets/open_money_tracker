package com.blogspot.e_kanivets.moneytracker.activity;

import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.activity.base.BaseBackActivity;

import butterknife.BindView;

public class AboutActivity extends BaseBackActivity {
    @BindView(R.id.tv_about)
    TextView tvAbout;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_about;
    }

    @Override
    protected void initViews() {
        super.initViews();
        tvAbout.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
