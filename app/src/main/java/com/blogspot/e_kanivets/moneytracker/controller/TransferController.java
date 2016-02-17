package com.blogspot.e_kanivets.moneytracker.controller;

import com.blogspot.e_kanivets.moneytracker.model.Transfer;
import com.blogspot.e_kanivets.moneytracker.repo.IRepo;

/**
 * Controller class to encapsulate transfer handling logic.
 * Created on 2/17/16.
 *
 * @author Evgenii Kanivets
 */
public class TransferController {
    @SuppressWarnings("unused")
    private static final String TAG = "TransferController";

    private IRepo<Transfer> transferRepo;
    private AccountController accountController;

    public TransferController(IRepo<Transfer> transferRepo, AccountController accountController) {
        this.transferRepo = transferRepo;
        this.accountController = accountController;
    }

    @SuppressWarnings("SimplifiableIfStatement")
    public boolean create(Transfer transfer) {
        if (transferRepo.create(transfer) == null) return false;
        else return accountController.transferDone(transfer);
    }
}