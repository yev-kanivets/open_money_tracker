package com.blogspot.e_kanivets.moneytracker.model;

/**
 * Entity class for account
 * Created by evgenii on 6/3/15.
 */
public class Account {
    private int id;
    private String title;
    private int curSum;

    public Account(int id, String title, int curSum) {
        this.title = title;
        this.curSum = curSum;
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
}
