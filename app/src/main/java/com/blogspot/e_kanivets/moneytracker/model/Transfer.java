package com.blogspot.e_kanivets.moneytracker.model;

/**
 * Entity class to represent transfer operation between accounts.
 * Created on 2/10/16.
 *
 * @author Evgenii Kanivets
 *
 * + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT,"
+ TIME_COLUMN + " INTEGER,"
+ FROM_ACCOUNT_ID_COLUMN + " INTEGER,"
+ TO_ACCOUND_ID_COLUMN + " INTEGER,"
+ FROM_AMOUNT_COLUMN + " INTEGER,"
+ TO_AMOUNT_COLUMN + " INTEGER);");
 */
public class Transfer {
    private long id;
    private long time;
    private long fromAccountId;
    private long toAccountId;
    private int fromAmount;
    private int toAmount;

    public Transfer(long id, long time, long fromAccountId, long toAccountId, int fromAmount, int toAmount) {
        this.id = id;
        this.time = time;
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.fromAmount = fromAmount;
        this.toAmount = toAmount;
    }

    public Transfer(long time, long fromAccountId, long toAccountId, int fromAmount, int toAmount) {
        this.time = time;
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.fromAmount = fromAmount;
        this.toAmount = toAmount;
    }

    public long getId() {
        return id;
    }

    public long getTime() {
        return time;
    }

    public long getFromAccountId() {
        return fromAccountId;
    }

    public long getToAccountId() {
        return toAccountId;
    }

    public int getFromAmount() {
        return fromAmount;
    }

    public int getToAmount() {
        return toAmount;
    }
}