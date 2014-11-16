package com.blogspot.e_kanivets.moneytracker.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.DisplayMetrics;

/**
 * Created by eugene on 02/09/14.
 */
public class AppUtils {

    public static int scaleValue(Context context, int value) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return (int)(value * (metrics.densityDpi / 320.0));
    }

    /* Dealing with SharedPreferences section */

    public static void addLaunchCount(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(Constants.APP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(Constants.LAUNCH_COUNT, preferences.getInt(Constants.LAUNCH_COUNT, 0) + 1);
        editor.commit();
    }

    public static boolean checkRateDialog(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(Constants.APP_NAME, Context.MODE_PRIVATE);

        boolean appRated = preferences.getBoolean(Constants.APP_RATED, false);
        if(appRated) return false;

        int launchCount = preferences.getInt(Constants.LAUNCH_COUNT, 0);
        if(launchCount % Constants.RATE_PERIOD == 0) {
            return true;
        }
        else {
            return false;
        }
    }

    public static void appRated(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(Constants.APP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(Constants.APP_RATED, true);
        editor.commit();
    }
}
