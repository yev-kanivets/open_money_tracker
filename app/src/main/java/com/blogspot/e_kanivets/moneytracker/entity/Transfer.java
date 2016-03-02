package com.blogspot.e_kanivets.moneytracker.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.blogspot.e_kanivets.moneytracker.entity.base.BaseEntity;

/**
 * Entity class to represent transfer operation between accounts.
 * Created on 2/10/16.
 *
 * @author Evgenii Kanivets
 */
public class Transfer extends BaseEntity implements Parcelable {
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

    protected Transfer(Parcel in) {
        time = in.readLong();
        fromAccountId = in.readLong();
        toAccountId = in.readLong();
        fromAmount = in.readInt();
        toAmount = in.readInt();
    }

    public static final Creator<Transfer> CREATOR = new Creator<Transfer>() {
        @Override
        public Transfer createFromParcel(Parcel in) {
            return new Transfer(in);
        }

        @Override
        public Transfer[] newArray(int size) {
            return new Transfer[size];
        }
    };

    @Override
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

    @Override
    public boolean equals(Object o) {
        if (o instanceof Transfer) {
            Transfer transfer = (Transfer) o;
            return this.id == transfer.getId()
                    && this.time == transfer.getTime()
                    && this.fromAccountId == transfer.getFromAccountId()
                    && this.toAccountId == transfer.getToAccountId()
                    && this.fromAmount == transfer.getFromAmount()
                    && this.toAmount == transfer.getToAmount();
        } else return false;
    }

    @SuppressWarnings("StringBufferReplaceableByString")
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Transfer {");
        sb.append("id = ").append(id).append(", ");
        sb.append("time = ").append(time).append(", ");
        sb.append("fromAccountId = ").append(fromAccountId).append(", ");
        sb.append("toAccountId = ").append(toAccountId).append(", ");
        sb.append("fromAmount = ").append(fromAmount).append(", ");
        sb.append("toAmount = ").append(toAmount);
        sb.append("}");

        return sb.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(time);
        dest.writeLong(fromAccountId);
        dest.writeLong(toAccountId);
        dest.writeInt(fromAmount);
        dest.writeInt(toAmount);
    }
}