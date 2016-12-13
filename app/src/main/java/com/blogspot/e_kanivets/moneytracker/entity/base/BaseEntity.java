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

    protected long getLong(double value) {
        return (long) value;
    }

    protected long getDecimal(double value) {
        // Strange calculation because of double type precision issue
        return  (long) Math.round(value * 100 - getLong(value) * 100);
    }
}
