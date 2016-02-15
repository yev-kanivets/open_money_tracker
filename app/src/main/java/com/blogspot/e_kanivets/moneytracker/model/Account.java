package com.blogspot.e_kanivets.moneytracker.model;

/**
 * Entity class for account.
 * Created on 6/3/15.
 *
 * @author Evgenii Kanivets
 */
public class Account implements IEntity {
    private long id;
    private String title;
    private int curSum;
    private String currency;

    public Account(long id, String title, int curSum, String currency) {
        this.id = id;
        this.title = title;
        this.curSum = curSum;
        this.currency = currency;
    }

    public Account(String title, int curSum, String currency) {
        this.id = -1;
        this.title = title;
        this.curSum = curSum;
        this.currency = currency;
    }

    @Override
    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCurSum() {
        return curSum;
    }

    public void setCurSum(int curSum) {
        this.curSum = curSum;
    }

    public String getCurrency() {
        return currency;
    }

    public void put(int amount) {
        curSum += amount;
    }

    public void take(int amount) {
        curSum -= amount;
    }

    @SuppressWarnings("StringBufferReplaceableByString")
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Account {");
        sb.append("id = ").append(id).append(", ");
        sb.append("title = ").append(title).append(", ");
        sb.append("curSum = ").append(curSum).append(", ");
        sb.append("currency = ").append(currency);
        sb.append("}");

        return sb.toString();
    }

    @SuppressWarnings("SimplifiableIfStatement")
    @Override
    public boolean equals(Object o) {
        if (o instanceof Account) {
            return ((Account) o).getId() == getId();
        } else {
            return false;
        }
    }
}