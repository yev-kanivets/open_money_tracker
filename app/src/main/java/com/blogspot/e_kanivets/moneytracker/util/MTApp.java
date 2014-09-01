package com.blogspot.e_kanivets.moneytracker.util;

import android.app.Application;

import com.blogspot.e_kanivets.moneytracker.helper.DBHelper;

/**
 * Created by eugene on 29/08/14.
 */
public class MTApp extends Application{

    private static MTApp mtApp;

    private DBHelper dbHelper;

    public static MTApp get() {
        return mtApp;
    }

    public DBHelper getDbHelper() {
        return dbHelper;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mtApp = this;

        dbHelper = new DBHelper(mtApp);
    }
}
