package com.blogspot.e_kanivets.moneytracker.activity;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.blogspot.e_kanivets.moneytracker.R;

/**
 * Don't want to use {@link android.preference.PreferenceFragment}.
 * Know that SettingsActivity is deprecated, but it's more convenient for me.
 * Created on 3/23/16.
 *
 * @author Evgenii Kanivets
 */
public class SettingsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
