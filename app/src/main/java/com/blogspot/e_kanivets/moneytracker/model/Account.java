package com.blogspot.e_kanivets.moneytracker.model;

/**
 * Entity class for account
 * Created by evgenii on 6/3/15.
 */
public class Account {
    private int id;
    private String title;
    private int curSum;
    private String currency;

    public Account(int id, String title, int curSum, String currency) {
        this.id = id;
        this.title = title;
        this.curSum = curSum;
        this.currency = currency;
    }

    public int getId() {
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
