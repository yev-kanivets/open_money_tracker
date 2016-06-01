package com.blogspot.e_kanivets.moneytracker.controller;

import java.util.Locale;

/**
 * Controller class to encapsulate string format handling logic.
 * Not deal with {@link com.blogspot.e_kanivets.moneytracker.repo.base.IRepo} instances as others.
 * Created on 6/1/16.
 *
 * @author Evgenii Kanivets
 */
public class FormatController {
    public String formatAmount(double amount) {
        return String.format(Locale.getDefault(), "%.2f", amount);
    }

    public String formatSignedAmount(double amount) {
        return (amount >= 0.0 ? "+ " : "- ") + formatAmount(Math.abs(amount));
    }

    public String formatIncome(double amount, String currency) {
        return (amount >= 0 ? "+ " : "- ") + formatAmount(Math.abs(amount)) + " " + currency;
    }

    public String formatExpense(double amount, String currency) {
        return (amount > 0 ? "+ " : "- ") + formatAmount(Math.abs(amount)) + " " + currency;
    }
}
