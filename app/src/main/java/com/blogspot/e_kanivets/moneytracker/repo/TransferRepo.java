package com.blogspot.e_kanivets.moneytracker.repo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.blogspot.e_kanivets.moneytracker.DbHelper;
import com.blogspot.e_kanivets.moneytracker.controller.AccountController;
import com.blogspot.e_kanivets.moneytracker.model.Transfer;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link IRepo} implementation for {@link Transfer} entity.
 * Created on 2/15/16.
 *
 * @author Evgenii Kanivets
 */
public class TransferRepo extends BaseRepo<Transfer> {
    @SuppressWarnings("unused")
    private static final String TAG = "TransferRepo";

    public TransferRepo(DbHelper dbHelper) {
        super(dbHelper);
    }

    @Override
    protected String getTable() {
        return DbHelper.TABLE_TRANSFERS;
    }

    @Nullable
    @Override
    public Transfer create(Transfer transfer) {
        if (transfer == null) return null;

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DbHelper.TIME_COLUMN, transfer.getTime());
        contentValues.put(DbHelper.FROM_ACCOUNT_ID_COLUMN, transfer.getFromAccountId());
        contentValues.put(DbHelper.TO_ACCOUNT_ID_COLUMN, transfer.getToAccountId());
        contentValues.put(DbHelper.FROM_AMOUNT_COLUMN, transfer.getFromAmount());
        contentValues.put(DbHelper.TO_AMOUNT_COLUMN, transfer.getToAmount());

        long id = db.insert(getTable(), null, contentValues);

        db.close();

        if (id == -1) {
            Log.d(TAG, "Couldn't create transfer : " + transfer);
            return null;
        } else {
            Transfer createdTransfer = read(id);
            Log.d(TAG, "Created transfer : " + createdTransfer);
            return createdTransfer;
        }
    }

    @Nullable
    @Override
    public Transfer update(Transfer instance) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    protected List<Transfer> getListFromCursor(Cursor cursor) {
        List<Transfer> accountList = new ArrayList<>();

        if (cursor.moveToFirst()) {
            // Get indexes of columns
            int idColIndex = cursor.getColumnIndex(DbHelper.ID_COLUMN);
            int idColTime = cursor.getColumnIndex(DbHelper.TIME_COLUMN);
            int idColFromAccountId = cursor.getColumnIndex(DbHelper.FROM_ACCOUNT_ID_COLUMN);
            int idColToAccountId = cursor.getColumnIndex(DbHelper.TO_ACCOUNT_ID_COLUMN);
            int idColFromAmount = cursor.getColumnIndex(DbHelper.FROM_AMOUNT_COLUMN);
            int idColToAmount = cursor.getColumnIndex(DbHelper.TO_AMOUNT_COLUMN);

            do {
                // Read a account from DB
                Transfer account = new Transfer(cursor.getInt(idColIndex),
                        cursor.getLong(idColTime),
                        cursor.getLong(idColFromAccountId),
                        cursor.getLong(idColToAccountId),
                        cursor.getInt(idColFromAmount),
                        cursor.getInt(idColToAmount));

                accountList.add(account);
            } while (cursor.moveToNext());
        }

        return accountList;
    }
}