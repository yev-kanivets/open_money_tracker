package com.blogspot.e_kanivets.moneytracker.util;

import android.app.Application;

import com.blogspot.e_kanivets.moneytracker.helper.MTHelper;

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

        MTHelper.getInstance().initialize();
    }
}
