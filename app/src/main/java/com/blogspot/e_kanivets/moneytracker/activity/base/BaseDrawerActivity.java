package com.blogspot.e_kanivets.moneytracker.activity.base;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.activity.ChartsActivity;
import com.blogspot.e_kanivets.moneytracker.activity.ExportActivity;
import com.blogspot.e_kanivets.moneytracker.activity.SettingsActivity;
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
    private static final int REQUEST_ACCOUNTS = 1;
    private static final int REQUEST_RATES = 2;
    private static final int REQUEST_SETTINGS = 3;

    @Bind(R.id.drawer_layout)
    DrawerLayout drawer;
    @Bind(R.id.nav_view)
    protected NavigationView navigationView;

    protected abstract void update();

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
        if (drawer != null) drawer.setDrawerListener(toggle);
        toggle.syncState();

        return toolbar;
    }

    @Override
    protected void initViews() {
        super.initViews();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_accounts:
                startActivityForResult(new Intent(BaseDrawerActivity.this, AccountsActivity.class),
                        REQUEST_ACCOUNTS);
                break;

            case R.id.nav_rates:
                startActivityForResult(new Intent(BaseDrawerActivity.this, ExchangeRatesActivity.class),
                        REQUEST_RATES);
                break;

            case R.id.nav_charts:
                startActivity(new Intent(BaseDrawerActivity.this, ChartsActivity.class));
                break;

            case R.id.nav_export:
                startActivity(new Intent(BaseDrawerActivity.this, ExportActivity.class));
                break;

            case R.id.nav_settings:
                startActivityForResult(new Intent(BaseDrawerActivity.this, SettingsActivity.class),
                        REQUEST_SETTINGS);
                break;

            default:
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_ACCOUNTS:
                    update();
                    break;

                case REQUEST_RATES:
                    update();
                    break;

                case REQUEST_SETTINGS:
                    update();
                    break;

                default:
                    break;
            }
        }
    }
}
