package com.blogspot.e_kanivets.moneytracker.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;

/**
 * Created by eugene on 02/09/14.
 */
public class AppUtils {

    public static int scaleValue(Context context, int value) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return (int)(value * (metrics.densityDpi / 320.0));
    }
}
