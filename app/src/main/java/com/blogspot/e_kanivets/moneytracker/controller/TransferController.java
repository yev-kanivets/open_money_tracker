package com.blogspot.e_kanivets.moneytracker.controller;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.util.Log;

import com.blogspot.e_kanivets.moneytracker.DbHelper;
import com.blogspot.e_kanivets.moneytracker.model.Transfer;

/**
 * Controller class to encapsulate transfer handling logic.
 * Created by evgenii_kanivets on 2/10/16.
 */
public class TransferController {
    @SuppressWarnings("unused")
    private static final String TAG = "TransferController";

    private DbHelper dbHelper;
    private AccountController accountController;

    public TransferController(DbHelper dbHelper, AccountController accountController) {
        this.dbHelper = dbHelper;
        this.accountController = accountController;
    }

    public boolean create(@Nullable Transfer transfer) {
        if (transfer == null) return false;

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DbHelper.TIME_COLUMN, transfer.getTime());
        contentValues.put(DbHelper.FROM_ACCOUNT_ID_COLUMN, transfer.getFromAccountId());
        contentValues.put(DbHelper.TO_ACCOUND_ID_COLUMN, transfer.getToAccountId());
        contentValues.put(DbHelper.FROM_AMOUNT_COLUMN, transfer.getFromAmount());
        contentValues.put(DbHelper.TO_AMOUNT_COLUMN, transfer.getToAmount());

        long id = db.insert(DbHelper.TABLE_TRANSFERS, null, contentValues);
        Log.d(TAG, "created transfer with id = " + id);

        db.close();

        accountController.transferDone(transfer);

        return true;
    }
}