package com.blogspot.e_kanivets.moneytracker.helper;

import java.util.Observable;

/**
 * Helper class for Money Tracker application. Singleton.
 * Created on 01/09/14.
 *
 * @author Evgenii Kanivets
 */
public class MtHelper extends Observable {
    private static MtHelper mtHelper;

    public static MtHelper getInstance() {
        if (mtHelper == null) mtHelper = new MtHelper();
        return mtHelper;
    }

    public void update() {
        //notify observers
        setChanged();
        notifyObservers();
    }
}