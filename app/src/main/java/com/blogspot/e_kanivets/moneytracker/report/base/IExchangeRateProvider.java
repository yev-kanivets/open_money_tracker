package com.blogspot.e_kanivets.moneytracker.report.base;

import android.support.annotation.Nullable;

import com.blogspot.e_kanivets.moneytracker.entity.ExchangeRate;
import com.blogspot.e_kanivets.moneytracker.entity.Record;

/**
 * Interface that represents a contract of access to currency exchange rate.
 * Created on 2/25/16.
 *
 * @author Evgenii Kanivets
 */
public interface IExchangeRateProvider {
    /**
     * Gives an exchange rate for given record.
     *
     * @param record to request an exchange rate for
     * @return exchange rate
     */
    @Nullable
    ExchangeRate getRate(@Nullable Record record);
}