package com.blogspot.e_kanivets.moneytracker.controller;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.util.Log;

import com.blogspot.e_kanivets.moneytracker.DbHelper;
import com.blogspot.e_kanivets.moneytracker.model.Account;
import com.blogspot.e_kanivets.moneytracker.model.Record;
import com.blogspot.e_kanivets.moneytracker.model.Transfer;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller class to encapsulate account handling logic.
 * Created on 1/23/16.
 *
 * @author Evgenii Kanivets
 */
public class AccountController {
    private static final String TAG = "AccountController";

    private DbHelper dbHelper;

    public AccountController(DbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public List<Account> getAccounts() {
        List<Account> accountList = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Read accounts table from db
        Cursor cursor = db.query(DbHelper.TABLE_ACCOUNTS, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            // Get indexes of columns
            int idColIndex = cursor.getColumnIndex(DbHelper.ID_COLUMN);
            int titleColIndex = cursor.getColumnIndex(DbHelper.TITLE_COLUMN);
            int curSumColIndex = cursor.getColumnIndex(DbHelper.CUR_SUM_COLUMN);
            int currencyColIndex = cursor.getColumnIndex(DbHelper.CURRENCY_COLUMN);

            do {
                // Read a account from DB
                Account account = new Account(cursor.getInt(idColIndex),
                        cursor.getString(titleColIndex),
                        cursor.getInt(curSumColIndex),
                        cursor.getString(currencyColIndex));

                //Add account to list
                accountList.add(account);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return accountList;
    }

    @Nullable
    public Account read(long id) {
        Account account = null;

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Read accounts table from db
        Cursor cursor = db.query(DbHelper.TABLE_ACCOUNTS, null, "id=?",
                new String[]{Long.toString(id)}, null, null, null);

        if (cursor.moveToFirst()) {
            // Get indexes of columns
            int idColIndex = cursor.getColumnIndex(DbHelper.ID_COLUMN);
            int titleColIndex = cursor.getColumnIndex(DbHelper.TITLE_COLUMN);
            int curSumColIndex = cursor.getColumnIndex(DbHelper.CUR_SUM_COLUMN);
            int currencyColIndex = cursor.getColumnIndex(DbHelper.CURRENCY_COLUMN);

            do {
                // Read a account from DB
                account = new Account(cursor.getInt(idColIndex),
                        cursor.getString(titleColIndex),
                        cursor.getInt(curSumColIndex),
                        cursor.getString(currencyColIndex));

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return account;
    }

    public void update(Account account) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DbHelper.CUR_SUM_COLUMN, account.getCurSum());

        db.update(DbHelper.TABLE_ACCOUNTS, contentValues, "id=?",
                new String[]{Integer.valueOf(account.getId()).toString()});

        db.close();
    }

    public void deleteAccount(Account account) {
        // Delete the account from DB
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DbHelper.TABLE_ACCOUNTS, "id=?", new String[]{Integer.toString(account.getId())});
        db.close();
    }

    public void addAccount(String title, int curSum, String currency) {
        //Add account to DB
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DbHelper.TITLE_COLUMN, title);
        contentValues.put(DbHelper.CUR_SUM_COLUMN, curSum);
        contentValues.put(DbHelper.CURRENCY_COLUMN, currency);

        db.insert(DbHelper.TABLE_ACCOUNTS, null, contentValues);

        db.close();
    }

    public boolean recordAdded(@Nullable Record record) {
        if (record == null) return false;

        Account account = read(record.getAccountId());
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

        Log.d(TAG, "recordAdded: " + account);

        update(account);

        return true;
    }

    public boolean recordDeleted(@Nullable Record record) {
        if (record == null) return false;

        Account account = read(record.getAccountId());
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

        update(account);

        return true;
    }

    @SuppressWarnings("SimplifiableIfStatement")
    public boolean recordUpdated(@Nullable Record oldRecord, @Nullable Record newRecord) {
        if (oldRecord == null || newRecord == null) return false;

        return recordDeleted(oldRecord) && recordAdded(newRecord);
    }

    public boolean transferDone(@Nullable Transfer transfer) {
        if(transfer == null) return false;

        Account fromAccount = read(transfer.getFromAccountId());
        Account toAccount = read(transfer.getToAccountId());

        if (fromAccount == null || toAccount == null) return false;

        fromAccount.take(transfer.getFromAmount());
        toAccount.put(transfer.getToAmount());

        update(fromAccount);
        update(toAccount);

        return true;
    }
}