package com.blogspot.e_kanivets.moneytracker.controller;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Controller class to encapsulate string format handling logic. Locale.US is used to prevent
 * comma/dot problem when display/parse decimals.
 * Not deal with {@link com.blogspot.e_kanivets.moneytracker.repo.base.IRepo} instances as others.
 * Created on 6/1/16.
 *
 * @author Evgenii Kanivets
 */
public class FormatController {
    public static final String PRECISION_MATH = "precision_math";
    public static final String PRECISION_INT = "precision_int";
    public static final String PRECISION_NONE = "precision_none";

    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat shortDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat fullDateFormat = new SimpleDateFormat("d MMMM yyyy");
    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat dateAndTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private PreferenceController preferenceController;

    public FormatController(PreferenceController preferenceController) {
        this.preferenceController = preferenceController;
    }

    public String formatAmount(double amount) {
        switch (preferenceController.readDisplayPrecision()) {
            case PRECISION_MATH:
                return formatPrecisionMath(amount);

            case PRECISION_INT:
                return formatPrecisionInt(amount);

            case PRECISION_NONE:
                return formatPrecisionNone(amount);

            default:
                return formatPrecisionMath(amount);
        }
    }

    public String formatPrecisionMath(double amount) {
        return String.format(Locale.US, "%d", Math.round(amount));
    }

    public String formatPrecisionInt(double amount) {
        return String.format(Locale.US, "%d", (int) amount);
    }

    public String formatPrecisionNone(double amount) {
        return String.format(Locale.US, "%.2f", amount);
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

    public String formatDateToNumber(long timestamp) {
        return shortDateFormat.format(new Date(timestamp));
    }

    public String formatDateToString(long timestamp) {
        return fullDateFormat.format(new Date(timestamp));
    }

    public String formatTime(long timestamp) {
        return timeFormat.format(new Date(timestamp));
    }

    public String formatDateAndTime(long timestamp) {
        return dateAndTimeFormat.format(new Date(timestamp));
    }
}
