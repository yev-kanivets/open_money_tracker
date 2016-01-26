package com.blogspot.e_kanivets.moneytracker.controller;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.blogspot.e_kanivets.moneytracker.helper.DbHelper;
import com.blogspot.e_kanivets.moneytracker.model.Account;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller class to encapsulate account handling logic.
 * Created on 1/23/16.
 *
 * @author Evgenii Kanivets
 */
public class AccountController {
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

            do {
                // Read a account from DB
                Account account = new Account(cursor.getInt(idColIndex),
                        cursor.getString(titleColIndex),
                        cursor.getInt(curSumColIndex));

                //Add account to list
                accountList.add(account);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return accountList;
    }

    public void updateAccountById(int id, int diff) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Read account from db
        Cursor cursor = db.query(DbHelper.TABLE_ACCOUNTS, null, "id=?", new String[]{Integer.valueOf(id).toString()}, null, null, null);
        Account account = null;
        if (cursor.moveToFirst()) {
            // Get indexes of columns
            int idColIndex = cursor.getColumnIndex(DbHelper.ID_COLUMN);
            int titleColIndex = cursor.getColumnIndex(DbHelper.TITLE_COLUMN);
            int curSumColIndex = cursor.getColumnIndex(DbHelper.CUR_SUM_COLUMN);

            account = new Account(cursor.getInt(idColIndex),
                    cursor.getString(titleColIndex),
                    cursor.getInt(curSumColIndex));
        }

        cursor.close();

        if (account != null) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DbHelper.CUR_SUM_COLUMN, account.getCurSum() + diff);

            db.update(DbHelper.TABLE_ACCOUNTS, contentValues, "id=?", new String[]{Integer.valueOf(id).toString()});
        }
    }

    public void deleteAccount(Account account) {
        // Delete the account from DB
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DbHelper.TABLE_ACCOUNTS, "id=?",
                new String[]{Integer.toString(account.getId())});
        db.close();
    }

    public void addAccount(String title, int curSum) {
        //Add account to DB
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DbHelper.TITLE_COLUMN, title);
        contentValues.put(DbHelper.CUR_SUM_COLUMN, curSum);

        int id = (int) db.insert(DbHelper.TABLE_ACCOUNTS, null, contentValues);

        db.close();
    }
}