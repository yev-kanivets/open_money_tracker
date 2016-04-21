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

    protected Account(Parcel in) {
        id = in.readLong();
        title = in.readString();
        curSum = in.readInt();
        currency = in.readString();
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

    public int getCurSum() {
        return curSum;
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

    @SuppressWarnings("SimplifiableIfStatement")
    @Override
    public boolean equals(Object o) {
        if (o instanceof Account) {
            Account account = (Account) o;
            return this.id == account.getId()
                    && equals(this.title, account.getTitle())
                    && this.curSum == account.getCurSum()
                    && equals(this.currency, account.getCurrency());
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
        sb.append("currency = ").append(currency);
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
        dest.writeInt(curSum);
        dest.writeString(currency);
    }
}