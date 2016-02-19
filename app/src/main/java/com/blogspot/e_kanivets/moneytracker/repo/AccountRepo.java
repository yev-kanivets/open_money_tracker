package com.blogspot.e_kanivets.moneytracker.repo;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;

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

    @NonNull
    @Override
    protected ContentValues contentValues(Account account) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbHelper.TITLE_COLUMN, account.getTitle());
        contentValues.put(DbHelper.CUR_SUM_COLUMN, account.getCurSum());
        contentValues.put(DbHelper.CURRENCY_COLUMN, account.getCurrency());

        return contentValues;
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