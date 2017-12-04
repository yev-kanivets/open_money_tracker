package com.blogspot.e_kanivets.moneytracker.entity.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.blogspot.e_kanivets.moneytracker.entity.base.BaseEntity;

/**
 * Entity class for account.
 * Created on 6/3/15.
 *
 * @author Evgenii Kanivets
 */
public class Account extends BaseEntity implements Parcelable {
    private final String title;
    private long curSum;
    private final String currency;
    private long decimals;
    private double goal;
    private boolean archived;
    private int color;

    public Account(long id, String title, long curSum, String currency, long decimals, double goal,
                   boolean archived, int color) {
        this.id = id;
        this.title = title;
        this.curSum = curSum;
        this.currency = currency;
        this.decimals = decimals;
        this.goal = goal;
        this.archived = archived;
        this.color = color;
    }

    public Account(long id, String title, double curSum, String currency, double goal,
                   boolean archived, int color) {
        this.id = id;
        this.title = title;
        this.currency = currency;
        this.curSum = getLong(curSum);
        this.decimals = getDecimal(curSum);
        this.goal = goal;
        this.archived = archived;
        this.color = color;
    }

    protected Account(Parcel in) {
        id = in.readLong();
        title = in.readString();
        curSum = in.readLong();
        currency = in.readString();
        decimals = in.readLong();
        goal = in.readDouble();
        archived = in.readByte() != 0;
        color = in.readInt();
    }

    public static final Creator<Account> CREATOR = new Creator<Account>() {
        @Override
        public Account createFromParcel(Parcel in) {
            return new Account(in);
        }

        @Override
        public Account[] newArray(int size) {
            return new Account[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public long getCurSum() {
        return curSum;
    }

    public long getDecimals() {
        return decimals;
    }

    public double getFullSum() {
        return getCurSum() + getDecimals() / 100.0;
    }

    public String getCurrency() {
        return currency;
    }

    public double getGoal() {
        return goal;
    }

    public boolean isArchived() {
        return archived;
    }

    public int getColor() {
        return color;
    }

    public void put(double amount) {
        double sum = curSum + decimals / 100.0;
        sum += amount;
        curSum = getLong(sum);
        decimals = getDecimal(sum);
    }

    public void take(double amount) {
        double sum = curSum + decimals / 100.0;
        sum -= amount;
        curSum = getLong(sum);
        decimals = getDecimal(sum);
    }

    public void archive() {
        archived = true;
    }

    public void restore() {
        archived = false;
    }

    @SuppressWarnings("SimplifiableIfStatement")
    @Override
    public boolean equals(Object o) {
        if (o instanceof Account) {
            Account account = (Account) o;
            return this.id == account.getId()
                    && equals(this.title, account.getTitle())
                    && this.curSum == account.getCurSum()
                    && equals(this.currency, account.getCurrency())
                    && this.decimals == account.decimals
                    && this.goal == account.goal
                    && this.archived == account.archived
                    && this.color == account.color;
        } else return false;
    }

    @SuppressWarnings("StringBufferReplaceableByString")
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Account {");
        sb.append("id = ").append(id).append(", ");
        sb.append("title = ").append(title).append(", ");
        sb.append("curSum = ").append(curSum).append(", ");
        sb.append("currency = ").append(currency).append(", ");
        sb.append("decimals = ").append(decimals).append(", ");
        sb.append("goal = ").append(goal).append(", ");
        sb.append("archived = ").append(archived).append(", ");
        sb.append("color = ").append(color);
        sb.append("}");

        return sb.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeLong(curSum);
        dest.writeString(currency);
        dest.writeLong(decimals);
        dest.writeDouble(goal);
        dest.writeByte((byte) (archived ? 1 : 0));
        dest.writeInt(color);
    }
}
