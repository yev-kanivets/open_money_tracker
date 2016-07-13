package com.blogspot.e_kanivets.moneytracker.controller.data;

import android.support.annotation.Nullable;

import com.blogspot.e_kanivets.moneytracker.controller.base.BaseController;
import com.blogspot.e_kanivets.moneytracker.entity.ExchangeRatePair;
import com.blogspot.e_kanivets.moneytracker.entity.data.ExchangeRate;
import com.blogspot.e_kanivets.moneytracker.repo.base.IRepo;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller class to encapsulate exchange rates handling logic.
 * Created on 2/23/16.
 *
 * @author Evgenii Kanivets
 */
public class ExchangeRateController extends BaseController<ExchangeRate> {
    public ExchangeRateController(IRepo<ExchangeRate> repo) {
        super(repo);
    }

    public void deleteExchangeRatePair(@Nullable ExchangeRatePair pair) {
        if (pair == null) return;

        List<ExchangeRate> rateListToRemove = new ArrayList<>();
        for (ExchangeRate rate : readAll()) {
            if (rate.getFromCurrency().equals(pair.getFromCurrency())
                    && rate.getToCurrency().equals(pair.getToCurrency()))
                rateListToRemove.add(rate);
            if (rate.getFromCurrency().equals(pair.getToCurrency())
                    && rate.getToCurrency().equals(pair.getFromCurrency()))
                rateListToRemove.add(rate);
        }

        for (ExchangeRate rate : rateListToRemove) {
            delete(rate);
        }
    }
}
