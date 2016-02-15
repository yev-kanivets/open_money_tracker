package com.blogspot.e_kanivets.moneytracker.repo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.blogspot.e_kanivets.moneytracker.DbHelper;
import com.blogspot.e_kanivets.moneytracker.model.Account;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link IRepo} implementation for {@link Account} entity.
 * Created on 2/15/16.
 *
 * @author Evgenii Kanivets
 */
public class AccountRepo implements IRepo<Account> {
    @SuppressWarnings("unused")
    private static final String TAG = "AccountRepo";

    private DbHelper dbHelper;

    public AccountRepo(DbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    @Nullable
    @Override
    public Account create(Account account) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DbHelper.TITLE_COLUMN, account.getTitle());
        contentValues.put(DbHelper.CUR_SUM_COLUMN, account.getCurSum());
        contentValues.put(DbHelper.CURRENCY_COLUMN, account.getCurrency());

        long id = db.insert(DbHelper.TABLE_ACCOUNTS, null, contentValues);

        db.close();

        if (id == -1) {
            Log.d(TAG, "Couldn't create account : " + account);
            return null;
        } else {
            Account createdAccount = read(id);
            Log.d(TAG, "Created account : " + createdAccount);
            return createdAccount;
        }
    }

    @Nullable
    @Override
    public Account read(long id) {
        List<Account> accountList = readWithCondition("id=?", new String[]{Long.toString(id)});

        if (accountList.size() == 1) return accountList.get(0);
        else return null;
    }

    @Nullable
    @Override
    public Account update(Account account) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DbHelper.CUR_SUM_COLUMN, account.getCurSum());
        contentValues.put(DbHelper.TITLE_COLUMN, account.getTitle());
        contentValues.put(DbHelper.CURRENCY_COLUMN, account.getCurrency());

        String[] args = new String[]{Integer.valueOf(account.getId()).toString()};
        long rowsAffected = db.update(DbHelper.TABLE_ACCOUNTS, contentValues, "id=?", args);

        db.close();

        if (rowsAffected == 0) {
            Log.d(TAG, "Couldn't update account : " + account);
            return null;
        } else {
            Account updatedAccount = read(account.getId());
            Log.d(TAG, "Updated account : " + updatedAccount);
            return updatedAccount;
        }
    }

    @Override
    public boolean delete(Account account) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String[] args = new String[]{Integer.toString(account.getId())};
        long rowsAffected = db.delete(DbHelper.TABLE_ACCOUNTS, "id=?", args);

        db.close();

        Log.d(TAG, account + (rowsAffected == 0 ? " didn't " : " ") + "deleted");

        return rowsAffected != 0;
    }

    @NonNull
    @Override
    public List<Account> readAll() {
        return readWithCondition(null, null);
    }

    @NonNull
    @Override
    public List<Account> readWithCondition(String condition, String[] args) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Read accounts table from db
        Cursor cursor = db.query(DbHelper.TABLE_ACCOUNTS, null, condition, args, null, null, null);
        List<Account> accountList = getAccountListFromCursor(cursor);

        cursor.close();
        db.close();

        return accountList;
    }

    private List<Account> getAccountListFromCursor(Cursor cursor) {
        List<Account> accountList = new ArrayList<>();

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

                accountList.add(account);
            } while (cursor.moveToNext());
        }

        return accountList;
    }
}