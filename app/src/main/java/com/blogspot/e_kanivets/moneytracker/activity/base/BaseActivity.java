package com.blogspot.e_kanivets.moneytracker.activity.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.blogspot.e_kanivets.moneytracker.R;

import butterknife.ButterKnife;

/**
 * Base implementation of {@link android.support.v7.app.AppCompatActivity} to describe some common
 * methods.
 * Created on 1/26/16.
 *
 * @author Evgenii Kanivets
 */
public abstract class BaseActivity extends AppCompatActivity {
    @SuppressWarnings("unused")
    private static final String TAG = "BaseActivity";

    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());

        if (initData()) initViews();
        else finish();
    }

    @LayoutRes
    protected abstract int getContentViewId();

    protected boolean initData() {
        return true;
    }

    protected void initViews() {
        ButterKnife.bind(BaseActivity.this);
        toolbar = initToolbar();
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    protected abstract Toolbar initToolbar();

    protected void showToast(String message) {
        Toast.makeText(BaseActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    protected void showToast(@StringRes int messageId) {
        Toast.makeText(BaseActivity.this, messageId, Toast.LENGTH_SHORT).show();
    }
}