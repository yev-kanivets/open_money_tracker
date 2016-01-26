package com.blogspot.e_kanivets.moneytracker.util;

import android.app.Application;

/**
 * Custom application implementation.
 * Created on 29/08/14.
 *
 * @author Evgenii Kanivets
 */
public class MtApp extends Application {

    private static MtApp mtApp;

    public static MtApp get() {
        return mtApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mtApp = this;
    }
}