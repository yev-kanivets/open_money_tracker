package com.blogspot.e_kanivets.moneytracker;

import android.app.Application;

/**
 * Created by eugene on 29/08/14.
 */
public class MTApp extends Application{

    private static MTApp mtApp;

    public static MTApp get() {
        return mtApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mtApp = this;
    }
}
