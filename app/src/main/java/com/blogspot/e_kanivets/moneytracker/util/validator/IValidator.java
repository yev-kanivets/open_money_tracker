package com.blogspot.e_kanivets.moneytracker.util.validator;

/**
 * Interface of validators for data models. It may works with UI elements directly.
 * Created on 06.12.2016.
 *
 * @author Evgenii Kanivets
 */

public interface IValidator<T> {
    long MAX_ABS_VALUE = Integer.MAX_VALUE * 1024L;

    /**
     * @return true if validation passed or false otherwise
     */
    boolean validate();
}
