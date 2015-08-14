package com.blogspot.e_kanivets.moneytracker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.fragment.AccountsFragment;
import com.blogspot.e_kanivets.moneytracker.fragment.AddAccountFragment;
import com.blogspot.e_kanivets.moneytracker.fragment.ExportFragment;
import com.blogspot.e_kanivets.moneytracker.fragment.NavigationDrawerFragment;
import com.blogspot.e_kanivets.moneytracker.fragment.RecordsFragment;
import com.blogspot.e_kanivets.moneytracker.model.Record;
import com.blogspot.e_kanivets.moneytracker.util.AppUtils;

public class NavDrawerActivity extends AppCompatActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks,
        RecordsFragment.OnFragmentInteractionListener {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_drawer);

        /* Increment launch count */
        AppUtils.addLaunchCount(NavDrawerActivity.this);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
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

            case AddAccountFragment.TAG:
                mTitle = getString(R.string.title_add_account);
                break;

            case ExportFragment.TAG:
                mTitle = getString(R.string.title_export);
                break;

            case RecordsFragment.TAG:
                mTitle = getString(R.string.title_records);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAddIncomePressed() {
        startAddIncomeActivity(null, AddIncomeActivity.Mode.MODE_ADD);
    }

    @Override
    public void onAddExpensePressed() {
        startAddExpenseActivity(null, AddExpenseActivity.Mode.MODE_ADD);
    }

    @Override
    public void onEditRecord(Record record) {
        if (record.isIncome()) {
            startAddIncomeActivity(record, AddIncomeActivity.Mode.MODE_EDIT);
        } else {
            startAddExpenseActivity(record, AddExpenseActivity.Mode.MODE_EDIT);
        }
    }

    public void showAddAccountFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, AddAccountFragment.newInstance())
                .addToBackStack(AddAccountFragment.TAG)
                .commit();
    }

    private void startAddIncomeActivity(Record record, AddIncomeActivity.Mode mode) {
        Intent intent = new Intent(NavDrawerActivity.this, AddIncomeActivity.class);
        intent.putExtra(AddExpenseActivity.KEY_RECORD, record);
        intent.putExtra(AddExpenseActivity.KEY_MODE, mode);
        startActivity(intent);
    }

    private void startAddExpenseActivity(Record record, AddExpenseActivity.Mode mode) {
        Intent intent = new Intent(NavDrawerActivity.this, AddExpenseActivity.class);
        intent.putExtra(AddExpenseActivity.KEY_RECORD, record);
        intent.putExtra(AddExpenseActivity.KEY_MODE, mode);
        startActivity(intent);
    }
}