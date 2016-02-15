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
public class TransferRepo implements IRepo<Transfer> {
    @SuppressWarnings("unused")
    private static final String TAG = "TransferRepo";

    private DbHelper dbHelper;
    private AccountController accountController;

    public TransferRepo(DbHelper dbHelper, AccountController accountController) {
        this.dbHelper = dbHelper;
        this.accountController = accountController;
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

        long id = db.insert(DbHelper.TABLE_TRANSFERS, null, contentValues);

        db.close();

        if (id == -1) {
            Log.d(TAG, "Couldn't create transfer : " + transfer);
            return null;
        } else {
            Transfer createdTransfer = read(id);
            accountController.transferDone(transfer);
            Log.d(TAG, "Created transfer : " + createdTransfer);

            return createdTransfer;
        }
    }

    @Nullable
    @Override
    public Transfer read(long id) {
        List<Transfer> transferList = readWithCondition("id=?", new String[]{Long.toString(id)});

        if (transferList.size() == 1) return transferList.get(0);
        else return null;
    }

    @Nullable
    @Override
    public Transfer update(Transfer instance) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public boolean delete(Transfer instance) {
        throw new IllegalStateException("Not implemented yet");
    }

    @NonNull
    @Override
    public List<Transfer> readAll() {
        return readWithCondition(null, null);
    }

    @NonNull
    @Override
    public List<Transfer> readWithCondition(String condition, String[] args) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Read accounts table from db
        Cursor cursor = db.query(DbHelper.TABLE_TRANSFERS, null, condition, args, null, null, null);
        List<Transfer> accountList = getTransferListFromCursor(cursor);

        cursor.close();
        db.close();

        return accountList;
    }

    private List<Transfer> getTransferListFromCursor(Cursor cursor) {
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