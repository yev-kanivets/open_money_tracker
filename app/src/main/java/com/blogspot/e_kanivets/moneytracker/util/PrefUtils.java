package com.blogspot.e_kanivets.moneytracker.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.blogspot.e_kanivets.moneytracker.MtApp;

/**
 * Util class for application.
 * Created on 02/09/14.
 *
 * @author Evgenii Kanivets
 */
public class PrefUtils {
    public static void addLaunchCount() {
        SharedPreferences preferences = getDefaultPrefs();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(Constants.LAUNCH_COUNT, preferences.getInt(Constants.LAUNCH_COUNT, 0) + 1);
        editor.apply();
    }

    public static boolean checkRateDialog() {
        SharedPreferences preferences = getDefaultPrefs();

        boolean appRated = preferences.getBoolean(Constants.APP_RATED, false);
        if (appRated) return false;

        int launchCount = preferences.getInt(Constants.LAUNCH_COUNT, 0);
        return launchCount % Constants.RATE_PERIOD == 0;
    }

    public static void appRated() {
        SharedPreferences preferences = getDefaultPrefs();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(Constants.APP_RATED, true);
        editor.apply();
    }

    public static int readUsedPeriod() {
        SharedPreferences preferences = getDefaultPrefs();
        return preferences.getInt(Constants.KEY_USED_PERIOD, Constants.DEFAULT_USED_PERIOD);
    }

    public static void writeUsedPeriod(int usedPeriod) {
        SharedPreferences preferences = getDefaultPrefs();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(Constants.KEY_USED_PERIOD, usedPeriod);
        editor.apply();
    }

    private static SharedPreferences getDefaultPrefs() {
        return MtApp.get().getSharedPreferences(MtApp.get().getPackageName(), Context.MODE_PRIVATE);
    }
}