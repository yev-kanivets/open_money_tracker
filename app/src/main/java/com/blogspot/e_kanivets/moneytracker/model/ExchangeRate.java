package com.blogspot.e_kanivets.moneytracker.model;

/**
 * Entity class to represent exchange rate between two currencies.
 * Created on 2/23/16.
 *
 * @author Evgenii Kanivets
 */
public class ExchangeRate implements IEntity {
    private long id;
    private long createdAt;
    private String fromCurrency;
    private String toCurrency;
    private double amount;

    public ExchangeRate(long id, long createdAt, String fromCurrency, String toCurrency, double amount) {
        this.id = id;
        this.createdAt = createdAt;
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        this.amount = amount;
    }

    public ExchangeRate(long createdAt, String fromCurrency, String toCurrency, double amount) {
        this.id = -1;
        this.createdAt = createdAt;
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        this.amount = amount;
    }

    @Override
    public long getId() {
        return id;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public String getFromCurrency() {
        return fromCurrency;
    }

    public String getToCurrency() {
        return toCurrency;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ExchangeRate) {
            ExchangeRate rate = (ExchangeRate) o;
            return this.id == rate.getId()
                    && this.createdAt == rate.getCreatedAt()
                    && this.fromCurrency.equals(rate.getFromCurrency())
                    && this.toCurrency.equals(rate.getToCurrency())
                    && this.amount == rate.getAmount();
        } else return false;
    }

    @SuppressWarnings("StringBufferReplaceableByString")
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ExchangeRate {");
        sb.append("id = ").append(id).append(", ");
        sb.append("createdAt = ").append(createdAt).append(", ");
        sb.append("fromCurrency = ").append(fromCurrency).append(", ");
        sb.append("toCurrency = ").append(toCurrency).append(", ");
        sb.append("amount = ").append(amount);
        sb.append("}");

        return sb.toString();
    }
}