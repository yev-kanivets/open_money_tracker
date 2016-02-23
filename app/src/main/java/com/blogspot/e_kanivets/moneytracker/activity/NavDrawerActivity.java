package com.blogspot.e_kanivets.moneytracker.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.view.Menu;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.activity.base.BaseActivity;
import com.blogspot.e_kanivets.moneytracker.fragment.AccountsFragment;
import com.blogspot.e_kanivets.moneytracker.fragment.ExchangeRatesFragment;
import com.blogspot.e_kanivets.moneytracker.fragment.ExportFragment;
import com.blogspot.e_kanivets.moneytracker.fragment.NavigationDrawerFragment;
import com.blogspot.e_kanivets.moneytracker.fragment.RecordsFragment;
import com.blogspot.e_kanivets.moneytracker.util.PrefUtils;

import butterknife.Bind;

public class NavDrawerActivity extends BaseActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {
    @SuppressWarnings("unused")
    private static final String TAG = "NavDrawerActivity";

    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_nav_drawer;
    }

    @Override
    protected boolean initData() {
        PrefUtils.addLaunchCount();
        return true;
    }

    @Override
    protected void initViews() {
        super.initViews();

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer, drawerLayout);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        Fragment fragment = new Fragment();

        switch (position + 1) {
            case 1:
                fragment = RecordsFragment.newInstance();
                break;

            case 2:
                fragment = AccountsFragment.newInstance();
                break;

            case 3:
                fragment = ExchangeRatesFragment.newInstance();
                break;

            case 4:
                fragment = ExportFragment.newInstance();
                break;

            default:
                break;
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    public void onSectionAttached(String tag) {
        switch (tag) {
            case AccountsFragment.TAG:
                mTitle = getString(R.string.title_accounts);
                break;

            case ExportFragment.TAG:
                mTitle = getString(R.string.title_export);
                break;

            case RecordsFragment.TAG:
                mTitle = getString(R.string.title_records);
                break;

            case ExchangeRatesFragment.TAG:
                mTitle = getString(R.string.title_exchange_rates);
                break;

            default:
                break;
        }

        restoreActionBar();
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //noinspection deprecation
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setTitle(mTitle);
            actionBar.setDisplayShowCustomEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            restoreActionBar();
            return true;
        }

        return super.onCreateOptionsMenu(menu);
    }
}