package com.blogspot.e_kanivets.moneytracker.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.model.Period;

/**
 * Controller class to encapsulate Shared Preferences handling logic.
 * Created on 4/21/16.
 *
 * @author Evgenii Kanivets
 */
public class PreferenceController {
    private static final String APP_RATED = "app_rated";
    private static final String LAUNCH_COUNT = "launch_count";
    private static final String KEY_FIRST_TS = "key_first_ts";
    private static final String KEY_LAST_TS = "key_last_ts";
    private static final String KEY_PERIOD_TYPE = "key_period_type";

    private static final int RATE_PERIOD = 5;

    @NonNull
    private Context context;

    public PreferenceController(@NonNull Context context) {
        this.context = context;
    }

    public void addLaunchCount() {
        SharedPreferences preferences = getDefaultPrefs();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(LAUNCH_COUNT, preferences.getInt(LAUNCH_COUNT, 0) + 1);
        editor.apply();
    }

    public boolean checkRateDialog() {
        SharedPreferences preferences = getDefaultPrefs();

        boolean appRated = preferences.getBoolean(APP_RATED, false);
        if (appRated) return false;

        int launchCount = preferences.getInt(LAUNCH_COUNT, 0);
        return launchCount % RATE_PERIOD == 0;
    }

    public void appRated() {
        SharedPreferences preferences = getDefaultPrefs();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(APP_RATED, true);
        editor.apply();
    }

    public Period readPeriod() {
        SharedPreferences preferences = getDefaultPrefs();
        long first = preferences.getLong(KEY_FIRST_TS, -1);
        long last = preferences.getLong(KEY_LAST_TS, -1);
        String type = preferences.getString(KEY_PERIOD_TYPE, null);

        if (first == -1 || last == -1 || type == null) return Period.weekPeriod();
        else {
            switch (type) {
                case Period.TYPE_DAY:
                    return Period.dayPeriod();

                case Period.TYPE_WEEK:
                    return Period.weekPeriod();

                case Period.TYPE_MONTH:
                    return Period.monthPeriod();

                case Period.TYPE_YEAR:
                    return Period.yearPeriod();

                case Period.TYPE_CUSTOM:
                    return Period.weekPeriod();

                default:
                    return Period.weekPeriod();

            }
        }
    }

    public long readDefaultAccountId() {
        String defaultAccountPref = context.getString(R.string.pref_default_account);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        return Long.parseLong(preferences.getString(defaultAccountPref, "-1"));
    }

    @Nullable
    public String readDefaultCurrency() {
        String defaultCurrencyPref = context.getString(R.string.pref_default_currency);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        return preferences.getString(defaultCurrencyPref, null);
    }

    public void writePeriod(Period period) {
        SharedPreferences preferences = getDefaultPrefs();
        SharedPreferences.Editor editor = preferences.edit();

        editor.putLong(KEY_FIRST_TS, period.getFirst().getTime());
        editor.putLong(KEY_LAST_TS, period.getLast().getTime());
        editor.putString(KEY_PERIOD_TYPE, period.getType());

        editor.apply();
    }

    private SharedPreferences getDefaultPrefs() {
        return context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }
}
