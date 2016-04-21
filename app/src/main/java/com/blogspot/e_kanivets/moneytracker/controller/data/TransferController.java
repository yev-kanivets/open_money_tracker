package com.blogspot.e_kanivets.moneytracker.controller.data;

import com.blogspot.e_kanivets.moneytracker.controller.base.BaseController;
import com.blogspot.e_kanivets.moneytracker.entity.data.Transfer;
import com.blogspot.e_kanivets.moneytracker.repo.base.IRepo;

/**
 * Controller class to encapsulate transfer handling logic.
 * Created on 2/17/16.
 *
 * @author Evgenii Kanivets
 */
public class TransferController extends BaseController<Transfer> {
    @SuppressWarnings("unused")
    private static final String TAG = "TransferController";

    private AccountController accountController;

    public TransferController(IRepo<Transfer> transferRepo, AccountController accountController) {
        super(transferRepo);
        this.accountController = accountController;
    }

    @Override
    @SuppressWarnings("SimplifiableIfStatement")
    public Transfer create(Transfer transfer) {
        Transfer createdTransfer = repo.create(transfer);

        if (createdTransfer == null) return null;
        else {
            accountController.transferDone(createdTransfer);
            return createdTransfer;
        }
    }
}