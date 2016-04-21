package com.blogspot.e_kanivets.moneytracker.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.blogspot.e_kanivets.moneytracker.R;

/**
 * Controller class to encapsulate Shared Preferences handling logic.
 * Not deal with {@link com.blogspot.e_kanivets.moneytracker.repo.base.IRepo} instances as others.
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

    public void writeFirstTs(long firstTs) {
        SharedPreferences preferences = getDefaultPrefs();
        SharedPreferences.Editor editor = preferences.edit();

        editor.putLong(KEY_FIRST_TS, firstTs);
        editor.apply();
    }

    public void writeLastTs(long lastTs) {
        SharedPreferences preferences = getDefaultPrefs();
        SharedPreferences.Editor editor = preferences.edit();

        editor.putLong(KEY_LAST_TS, lastTs);
        editor.apply();
    }

    public void writePeriodType(String periodType) {
        SharedPreferences preferences = getDefaultPrefs();
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString(KEY_PERIOD_TYPE, periodType);
        editor.apply();
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

    public long readFirstTs() {
        return getDefaultPrefs().getLong(KEY_FIRST_TS, -1);
    }

    public long readLastTs() {
        return getDefaultPrefs().getLong(KEY_LAST_TS, -1);
    }

    @Nullable
    public String readPeriodType() {
        return getDefaultPrefs().getString(KEY_PERIOD_TYPE, null);
    }

    private SharedPreferences getDefaultPrefs() {
        return context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }
}
