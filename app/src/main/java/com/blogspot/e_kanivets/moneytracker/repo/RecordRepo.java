package com.blogspot.e_kanivets.moneytracker.repo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.blogspot.e_kanivets.moneytracker.DbHelper;
import com.blogspot.e_kanivets.moneytracker.controller.AccountController;
import com.blogspot.e_kanivets.moneytracker.controller.CategoryController;
import com.blogspot.e_kanivets.moneytracker.model.Record;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link IRepo} implementation for {@link Record} entity.
 * Created on 2/15/16.
 *
 * @author Evgenii Kanivets
 */
public class RecordRepo extends BaseRepo<Record> {
    @SuppressWarnings("unused")
    private static final String TAG = "RecordRepo";

    public RecordRepo(DbHelper dbHelper) {
        super(dbHelper);
    }

    @Override
    protected String getTable() {
        return DbHelper.TABLE_RECORDS;
    }

    @NonNull
    @Override
    protected ContentValues contentValues(Record record) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbHelper.TIME_COLUMN, record.getTime());
        contentValues.put(DbHelper.TYPE_COLUMN, record.getType());
        contentValues.put(DbHelper.TITLE_COLUMN, record.getTitle());
        contentValues.put(DbHelper.CATEGORY_ID_COLUMN, record.getCategoryId());
        contentValues.put(DbHelper.PRICE_COLUMN, record.getPrice());
        contentValues.put(DbHelper.ACCOUNT_ID_COLUMN, record.getAccountId());

        return contentValues;
    }

    @Override
    protected List<Record> getListFromCursor(Cursor cursor) {
        List<Record> recordList = new ArrayList<>();

        if (cursor.moveToFirst()) {
            int idColIndex = cursor.getColumnIndex(DbHelper.ID_COLUMN);
            int timeColIndex = cursor.getColumnIndex(DbHelper.TIME_COLUMN);
            int typeColIndex = cursor.getColumnIndex(DbHelper.TYPE_COLUMN);
            int titleColIndex = cursor.getColumnIndex(DbHelper.TITLE_COLUMN);
            int categoryColIndex = cursor.getColumnIndex(DbHelper.CATEGORY_ID_COLUMN);
            int priceColIndex = cursor.getColumnIndex(DbHelper.PRICE_COLUMN);
            int accountIdColIndex = cursor.getColumnIndex(DbHelper.ACCOUNT_ID_COLUMN);

            do {
                Record record = new Record(cursor.getInt(idColIndex),
                        cursor.getLong(timeColIndex),
                        cursor.getInt(typeColIndex),
                        cursor.getString(titleColIndex),
                        cursor.getInt(categoryColIndex),
                        cursor.getInt(priceColIndex),
                        cursor.getInt(accountIdColIndex));

                recordList.add(record);
            } while (cursor.moveToNext());
        }

        return recordList;
    }
}