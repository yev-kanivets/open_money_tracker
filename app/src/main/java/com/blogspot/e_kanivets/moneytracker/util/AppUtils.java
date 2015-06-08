package com.blogspot.e_kanivets.moneytracker.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

/**
 * Util class for application
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
        editor.apply();
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
        editor.apply();
    }

    public static void addContribution(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(Constants.APP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(Constants.CONTRIBUTION, preferences.getInt(Constants.CONTRIBUTION, 0) + 1);
        editor.apply();
    }

    public static int getContribution(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(Constants.APP_NAME, Context.MODE_PRIVATE);
        return  preferences.getInt(Constants.CONTRIBUTION, 0);
    }

    public static int readUsedPeriod(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(Constants.APP_NAME, Context.MODE_PRIVATE);
        return  preferences.getInt(Constants.KEY_USED_PERIOD, Constants.DEFAULT_USED_PERIOD);
    }

    public static void writeUsedPeriod(Context context, int usedPeriod) {
        SharedPreferences preferences = context.getSharedPreferences(Constants.APP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(Constants.KEY_USED_PERIOD, usedPeriod);
        editor.apply();
    }

    public static boolean isKeyboardShown(View rootView) {
        /* 128dp = 32dp * 4, minimum button height 32dp and generic 4 rows soft keyboard */
        final int SOFT_KEYBOARD_HEIGHT_DP_THRESHOLD = 128;

        Rect r = new Rect();
        rootView.getWindowVisibleDisplayFrame(r);
        DisplayMetrics dm = rootView.getResources().getDisplayMetrics();
        /* heightDiff = rootView height - status bar height (r.top) - visible frame height (r.bottom - r.top) */
        int heightDiff = rootView.getBottom() - r.bottom;
        /* Threshold size: dp to pixels, multiply with display density */

        return heightDiff > SOFT_KEYBOARD_HEIGHT_DP_THRESHOLD * dm.density;
    }
}
