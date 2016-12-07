package com.blogspot.e_kanivets.moneytracker.util.validator;

import android.support.annotation.Nullable;

/**
 * Interface of validators for data models. It may works with UI elements directly.
 * Created on 06.12.2016.
 *
 * @author Evgenii Kanivets
 */

public interface IValidator<T> {
    long MAX_ABS_VALUE = Integer.MAX_VALUE * 1024L;

    /**
     * @return instance of class T if validation passed or null otherwise
     */
    @Nullable
    T validate();
}
