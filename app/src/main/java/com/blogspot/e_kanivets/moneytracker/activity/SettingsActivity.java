package com.blogspot.e_kanivets.moneytracker.activity;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.blogspot.e_kanivets.moneytracker.MtApp;
import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.activity.base.BaseBackActivity;
import com.blogspot.e_kanivets.moneytracker.controller.AccountController;
import com.blogspot.e_kanivets.moneytracker.entity.Account;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class SettingsActivity extends BaseBackActivity {
    @SuppressWarnings("unused")
    private static final String TAG = "SettingsActivity";

    @Override
    protected int getContentViewId() {
        return R.layout.activity_settings;
    }

    @Override
    protected void initViews() {
        super.initViews();

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(R.id.content, new SettingsFragment())
                .commit();
    }

    public static class SettingsFragment extends PreferenceFragment {
        @Inject
        AccountController accountController;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            MtApp.get().getAppComponent().inject(SettingsFragment.this);

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.preferences);

            ListPreference defaultAccountPref = (ListPreference) findPreference(getString(R.string.pref_default_account));
            defaultAccountPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    getActivity().setResult(RESULT_OK);
                    return true;
                }
            });

            List<Account> accountList = accountController.readAll();

            if (accountList.size() > 0)
                defaultAccountPref.setDefaultValue(Long.toString(accountList.get(0).getId()));
            defaultAccountPref.setEntries(getEntries(accountList));
            defaultAccountPref.setEntryValues(getEntryValues(accountList));
        }

        @SuppressWarnings("ToArrayCallWithZeroLengthArrayArgument")
        private String[] getEntries(List<Account> accountList) {
            List<String> result = new ArrayList<>();

            for (Account account : accountList) {
                result.add(account.getTitle());
            }

            return result.toArray(new String[0]);
        }

        @SuppressWarnings("ToArrayCallWithZeroLengthArrayArgument")
        private String[] getEntryValues(List<Account> accountList) {
            List<String> result = new ArrayList<>();

            for (Account account : accountList) {
                result.add(Long.toString(account.getId()));
            }

            return result.toArray(new String[0]);
        }
    }
}
