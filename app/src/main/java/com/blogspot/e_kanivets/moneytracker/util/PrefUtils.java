package com.blogspot.e_kanivets.moneytracker.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import com.blogspot.e_kanivets.moneytracker.MtApp;
import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.model.Period;

import java.util.Date;

/**
 * Util class for application.
 * Created on 02/09/14.
 *
 * @author Evgenii Kanivets
 */
public class PrefUtils {
    private static final String APP_RATED = "app_rated";
    private static final String LAUNCH_COUNT = "launch_count";
    private static final String KEY_FIRST_TS = "key_first_ts";
    private static final String KEY_LAST_TS = "key_last_ts";
    private static final String KEY_PERIOD_TYPE = "key_period_type";

    public static void addLaunchCount() {
        SharedPreferences preferences = getDefaultPrefs();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(LAUNCH_COUNT, preferences.getInt(LAUNCH_COUNT, 0) + 1);
        editor.apply();
    }

    public static boolean checkRateDialog() {
        SharedPreferences preferences = getDefaultPrefs();

        boolean appRated = preferences.getBoolean(APP_RATED, false);
        if (appRated) return false;

        int launchCount = preferences.getInt(LAUNCH_COUNT, 0);
        return launchCount % Constants.RATE_PERIOD == 0;
    }

    public static void appRated() {
        SharedPreferences preferences = getDefaultPrefs();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(APP_RATED, true);
        editor.apply();
    }

    public static Period readPeriod() {
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

    public static long readDefaultAccountId() {
        String defaultAccountPref = MtApp.get().getString(R.string.pref_default_account);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MtApp.get());

        return Long.parseLong(preferences.getString(defaultAccountPref, "-1"));
    }

    @Nullable
    public static String readDefaultCurrency() {
        String defaultCurrencyPref = MtApp.get().getString(R.string.pref_default_currency);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MtApp.get());

        return preferences.getString(defaultCurrencyPref, null);
    }

    public static void writePeriod(Period period) {
        SharedPreferences preferences = getDefaultPrefs();
        SharedPreferences.Editor editor = preferences.edit();

        editor.putLong(KEY_FIRST_TS, period.getFirst().getTime());
        editor.putLong(KEY_LAST_TS, period.getLast().getTime());
        editor.putString(KEY_PERIOD_TYPE, period.getType());

        editor.apply();
    }

    private static SharedPreferences getDefaultPrefs() {
        return MtApp.get().getSharedPreferences(MtApp.get().getPackageName(), Context.MODE_PRIVATE);
    }
}