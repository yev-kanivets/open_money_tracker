package com.blogspot.e_kanivets.moneytracker.activity.base;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.activity.ExportActivity;
import com.blogspot.e_kanivets.moneytracker.activity.account.AccountsActivity;
import com.blogspot.e_kanivets.moneytracker.activity.exchange_rate.ExchangeRatesActivity;

import butterknife.Bind;

/**
 * Base implementation of {@link android.support.v7.app.AppCompatActivity} to encapsulate Navigation
 * Drawer logic.
 * Created on 3/16/16.
 *
 * @author Evgenii Kanivets
 */
public abstract class BaseDrawerActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    @Bind(R.id.drawer_layout)
    DrawerLayout drawer;

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
        switch (item.getItemId()) {
            case R.id.nav_accounts:
                startActivity(new Intent(BaseDrawerActivity.this, AccountsActivity.class));
                break;

            case R.id.nav_rates:
                startActivity(new Intent(BaseDrawerActivity.this, ExchangeRatesActivity.class));
                break;

            case R.id.nav_export:
                startActivity(new Intent(BaseDrawerActivity.this, ExportActivity.class));
                break;

            default:
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return false;
    }
}
