package com.blogspot.e_kanivets.moneytracker.repo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
public class AccountRepo extends BaseRepo<Account> {
    @SuppressWarnings("unused")
    private static final String TAG = "AccountRepo";

    public AccountRepo(DbHelper dbHelper) {
        super(dbHelper);
    }

    @Override
    protected String getTable() {
        return DbHelper.TABLE_ACCOUNTS;
    }

    @Nullable
    @Override
    public Account create(Account account) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DbHelper.TITLE_COLUMN, account.getTitle());
        contentValues.put(DbHelper.CUR_SUM_COLUMN, account.getCurSum());
        contentValues.put(DbHelper.CURRENCY_COLUMN, account.getCurrency());

        long id = db.insert(getTable(), null, contentValues);

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
    public Account update(Account account) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DbHelper.CUR_SUM_COLUMN, account.getCurSum());
        contentValues.put(DbHelper.TITLE_COLUMN, account.getTitle());
        contentValues.put(DbHelper.CURRENCY_COLUMN, account.getCurrency());

        String[] args = new String[]{Long.valueOf(account.getId()).toString()};
        long rowsAffected = db.update(getTable(), contentValues, "id=?", args);

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
    protected List<Account> getListFromCursor(Cursor cursor) {
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