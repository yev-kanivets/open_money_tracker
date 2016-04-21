package com.blogspot.e_kanivets.moneytracker.repo.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.blogspot.e_kanivets.moneytracker.entity.data.Transfer;
import com.blogspot.e_kanivets.moneytracker.repo.DbHelper;
import com.blogspot.e_kanivets.moneytracker.repo.base.BaseRepo;
import com.blogspot.e_kanivets.moneytracker.repo.base.IRepo;

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

    @NonNull
    @Override
    protected String getTable() {
        return DbHelper.TABLE_TRANSFERS;
    }

    @Nullable
    @Override
    protected ContentValues contentValues(@Nullable Transfer transfer) {
        ContentValues contentValues = new ContentValues();
        if (transfer == null) return null;

        contentValues.put(DbHelper.TIME_COLUMN, transfer.getTime());
        contentValues.put(DbHelper.FROM_ACCOUNT_ID_COLUMN, transfer.getFromAccountId());
        contentValues.put(DbHelper.TO_ACCOUNT_ID_COLUMN, transfer.getToAccountId());
        contentValues.put(DbHelper.FROM_AMOUNT_COLUMN, transfer.getFromAmount());
        contentValues.put(DbHelper.TO_AMOUNT_COLUMN, transfer.getToAmount());

        return contentValues;
    }

    @NonNull
    @Override
    protected List<Transfer> getListFromCursor(@Nullable Cursor cursor) {
        List<Transfer> accountList = new ArrayList<>();
        if (cursor == null) return accountList;

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
                Transfer account = new Transfer(cursor.getLong(idColIndex),
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