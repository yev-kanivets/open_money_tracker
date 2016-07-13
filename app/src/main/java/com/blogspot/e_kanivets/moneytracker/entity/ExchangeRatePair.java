package com.blogspot.e_kanivets.moneytracker.entity;

/**
 * Not data entity that's used for {@link com.blogspot.e_kanivets.moneytracker.adapter.ExchangeRateAdapter}.
 * Created on 7/13/16.
 *
 * @author Evgenii Kanivets
 */
public class ExchangeRatePair {
    private String fromCurrency;
    private String toCurrency;
    private double amountBuy;
    private double amountSell;

    public ExchangeRatePair(String fromCurrency, String toCurrency, double amountBuy, double amountSell) {
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        this.amountBuy = amountBuy;
        this.amountSell = amountSell;
    }

    public ExchangeRatePair() {
    }

    public String getFromCurrency() {
        return fromCurrency;
    }

    public String getToCurrency() {
        return toCurrency;
    }

    public double getAmountBuy() {
        return amountBuy;
    }

    public double getAmountSell() {
        return amountSell;
    }

    public void setFromCurrency(String fromCurrency) {
        this.fromCurrency = fromCurrency;
    }

    public void setToCurrency(String toCurrency) {
        this.toCurrency = toCurrency;
    }

    public void setAmountBuy(double amountBuy) {
        this.amountBuy = amountBuy;
    }

    public void setAmountSell(double amountSell) {
        this.amountSell = amountSell;
    }
}
