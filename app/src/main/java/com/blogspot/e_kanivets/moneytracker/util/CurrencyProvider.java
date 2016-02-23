package com.blogspot.e_kanivets.moneytracker.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Currency;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Util class to handle currencies.
 * Created on 2/23/16.
 *
 * @author Evgenii Kanivets
 */
public class CurrencyProvider {
    public static List<String> getAllCurrencies() {
        Set<Currency> toret = new HashSet<>();
        Locale[] locs = Locale.getAvailableLocales();

        for (Locale loc : locs) {
            try {
                toret.add(Currency.getInstance(loc));
            } catch (Exception exc) {
                // Locale not found
            }
        }

        List<String> currencyList = new ArrayList<>();
        for (Currency currency : toret) {
            currencyList.add(currency.getCurrencyCode());
        }

        Collections.sort(currencyList);

        return currencyList;
    }
}