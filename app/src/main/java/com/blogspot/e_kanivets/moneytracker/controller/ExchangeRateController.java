package com.blogspot.e_kanivets.moneytracker.controller;

import com.blogspot.e_kanivets.moneytracker.controller.base.BaseController;
import com.blogspot.e_kanivets.moneytracker.model.ExchangeRate;
import com.blogspot.e_kanivets.moneytracker.repo.base.IRepo;

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
}