package com.blogspot.e_kanivets.moneytracker.activity;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.activity.account.AccountsActivity;
import com.blogspot.e_kanivets.moneytracker.activity.base.BaseActivity;
import com.blogspot.e_kanivets.moneytracker.activity.exchange_rate.ExchangeRatesActivity;
import com.blogspot.e_kanivets.moneytracker.fragment.RecordsFragment;
import com.blogspot.e_kanivets.moneytracker.util.PrefUtils;

import butterknife.Bind;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    @SuppressWarnings("unused")
    private static final String TAG = "MainActivity";

    @Bind(R.id.drawer_layout)
    DrawerLayout drawer;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected boolean initData() {
        PrefUtils.addLaunchCount();
        return true;
    }

    @Override
    protected void initViews() {
        super.initViews();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, RecordsFragment.newInstance())
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (drawer != null && drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
        else super.onBackPressed();
    }

    @Override
    protected Toolbar initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        return toolbar;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Fragment fragment = new Fragment();

        switch (item.getItemId()) {
            case R.id.nav_records:
                fragment = RecordsFragment.newInstance();
                break;

            case R.id.nav_accounts:
                startActivity(new Intent(MainActivity.this, AccountsActivity.class));
                break;

            case R.id.nav_rates:
                startActivity(new Intent(MainActivity.this, ExchangeRatesActivity.class));
                break;

            case R.id.nav_export:
                startActivity(new Intent(MainActivity.this, ExportActivity.class));
                break;

            default:
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return false;
    }
}