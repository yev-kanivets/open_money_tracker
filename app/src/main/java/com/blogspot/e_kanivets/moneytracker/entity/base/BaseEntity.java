package com.blogspot.e_kanivets.moneytracker.entity.base;

/**
 * Base class to encapsulate some common methods.
 * Created on 3/1/16.
 *
 * @author Evgenii Kanivets
 */
public abstract class BaseEntity implements IEntity {
    protected long id;

    @Override
    public long getId() {
        return id;
    }

    protected boolean equals(String str1, String str2) {
        if (str1 == null) return str2 == null;
        else return str1.equals(str2);
    }

    protected int getInteger(double value) {
        return (int) value;
    }

    protected int getDecimal(double value) {
        // Strange calculation because of double type precision issue
        return  (int) Math.round(value * 100 - getInteger(value) * 100);
    }
}
