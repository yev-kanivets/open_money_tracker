package com.blogspot.e_kanivets.moneytracker;

import android.app.Application;

import com.blogspot.e_kanivets.moneytracker.di.AppComponent;
import com.blogspot.e_kanivets.moneytracker.di.DaggerAppComponent;
import com.blogspot.e_kanivets.moneytracker.di.module.ControllerModule;
import com.blogspot.e_kanivets.moneytracker.di.module.RepoModule;

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

    private AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();

        mtApp = this;
        component = buildComponent();
    }

    public AppComponent getAppComponent() {
        return component;
    }

    private AppComponent buildComponent() {
        return DaggerAppComponent.builder()
                .repoModule(new RepoModule(get()))
                .controllerModule(new ControllerModule())
                .build();
    }
}