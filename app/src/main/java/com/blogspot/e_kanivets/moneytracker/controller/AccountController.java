package com.blogspot.e_kanivets.moneytracker.controller;

import android.support.annotation.Nullable;

import com.blogspot.e_kanivets.moneytracker.model.Account;
import com.blogspot.e_kanivets.moneytracker.model.Record;
import com.blogspot.e_kanivets.moneytracker.model.Transfer;
import com.blogspot.e_kanivets.moneytracker.repo.IRepo;

/**
 * Controller class to encapsulate account handling logic.
 * Created on 1/23/16.
 *
 * @author Evgenii Kanivets
 */
public class AccountController {
    @SuppressWarnings("unused")
    private static final String TAG = "AccountController";

    private IRepo<Account> accountRepo;

    public AccountController(IRepo<Account> accountRepo) {
        this.accountRepo = accountRepo;
    }

    public boolean recordAdded(@Nullable Record record) {
        if (record == null) return false;

        Account account = accountRepo.read(record.getAccountId());
        if (account == null) return false;

        switch (record.getType()) {
            case Record.TYPE_EXPENSE:
                account.take(record.getPrice());
                break;

            case Record.TYPE_INCOME:
                account.put(record.getPrice());
                break;

            default:
                break;
        }

        accountRepo.update(account);

        return true;
    }

    public boolean recordDeleted(@Nullable Record record) {
        if (record == null) return false;

        Account account = accountRepo.read(record.getAccountId());
        if (account == null) return false;

        switch (record.getType()) {
            case Record.TYPE_EXPENSE:
                account.put(record.getPrice());
                break;

            case Record.TYPE_INCOME:
                account.take(record.getPrice());
                break;

            default:
                break;
        }

        accountRepo.update(account);

        return true;
    }

    @SuppressWarnings("SimplifiableIfStatement")
    public boolean recordUpdated(@Nullable Record oldRecord, @Nullable Record newRecord) {
        if (oldRecord == null || newRecord == null) return false;

        return recordDeleted(oldRecord) && recordAdded(newRecord);
    }

    public boolean transferDone(@Nullable Transfer transfer) {
        if(transfer == null) return false;

        Account fromAccount = accountRepo.read(transfer.getFromAccountId());
        Account toAccount = accountRepo.read(transfer.getToAccountId());

        if (fromAccount == null || toAccount == null) return false;

        fromAccount.take(transfer.getFromAmount());
        toAccount.put(transfer.getToAmount());

        accountRepo.update(fromAccount);
        accountRepo.update(toAccount);

        return true;
    }
}