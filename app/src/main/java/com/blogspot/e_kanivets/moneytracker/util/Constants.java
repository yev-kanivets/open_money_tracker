package com.blogspot.e_kanivets.moneytracker.util;

/**
 * Constants class. It's better to change it to interface.
 * Created on 29/08/14.
 *
 * @author Evgenii Kanivets
 */
public interface Constants {
    String GP_MARKET = "market://details?id=";
    String APP_RATED = "app_rated";
    String LAUNCH_COUNT = "launch_count";
    int RATE_PERIOD = 5;
    String CONTRIBUTION = "contribution";
    String KEY_USED_PERIOD = "key_used_period";

    int DEFAULT_USED_PERIOD = 1;

    String TITLE_PARAM_NAME = "title";
    String PRICE_PARAM_NAME = "price";

    String DEFAULT_EXPORT_FILE_NAME = "money_tracker.csv";
}