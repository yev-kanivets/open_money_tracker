package com.blogspot.e_kanivets.moneytracker.controller;

import android.support.annotation.Nullable;

import com.blogspot.e_kanivets.moneytracker.controller.base.BaseController;
import com.blogspot.e_kanivets.moneytracker.entity.Account;
import com.blogspot.e_kanivets.moneytracker.entity.Record;
import com.blogspot.e_kanivets.moneytracker.entity.Transfer;
import com.blogspot.e_kanivets.moneytracker.repo.base.IRepo;
import com.blogspot.e_kanivets.moneytracker.util.PrefUtils;

import java.util.List;

/**
 * Controller class to encapsulate account handling logic.
 * Created on 1/23/16.
 *
 * @author Evgenii Kanivets
 */
public class AccountController extends BaseController<Account> {
    @SuppressWarnings("unused")
    private static final String TAG = "AccountController";

    public AccountController(IRepo<Account> accountRepo) {
        super(accountRepo);
    }

    public boolean recordAdded(@Nullable Record record) {
        if (record == null || record.getAccount() == null) return false;

        Account account = repo.read(record.getAccount().getId());
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

        repo.update(account);

        return true;
    }

    public boolean recordDeleted(@Nullable Record record) {
        if (record == null || record.getAccount() == null) return false;

        Account account = repo.read(record.getAccount().getId());
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

        repo.update(account);

        return true;
    }

    @SuppressWarnings("SimplifiableIfStatement")
    public boolean recordUpdated(@Nullable Record oldRecord, @Nullable Record newRecord) {
        if (oldRecord == null || newRecord == null) return false;

        return recordDeleted(oldRecord) && recordAdded(newRecord);
    }

    public boolean transferDone(@Nullable Transfer transfer) {
        if (transfer == null) return false;

        Account fromAccount = repo.read(transfer.getFromAccountId());
        Account toAccount = repo.read(transfer.getToAccountId());

        if (fromAccount == null || toAccount == null) return false;

        fromAccount.take(transfer.getFromAmount());
        toAccount.put(transfer.getToAmount());

        repo.update(fromAccount);
        repo.update(toAccount);

        return true;
    }

    @Nullable
    public Account readDefaultAccount() {
        long defaultAccountId = PrefUtils.readDefaultAccountId();

        if (defaultAccountId == -1) return getFirstAccount();
        else {
            Account account = read(defaultAccountId);
            if (account == null) return getFirstAccount();
            else return account;
        }
    }

    private Account getFirstAccount() {
        List<Account> accountList = readAll();
        if (accountList.size() == 0) return null;
        else return accountList.get(0);
    }
}