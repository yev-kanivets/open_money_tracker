package com.blogspot.e_kanivets.moneytracker.repo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

    @Nullable
    @Override
    public Record create(Record record) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DbHelper.TIME_COLUMN, record.getTime());
        contentValues.put(DbHelper.TYPE_COLUMN, record.getType());
        contentValues.put(DbHelper.TITLE_COLUMN, record.getTitle());
        contentValues.put(DbHelper.CATEGORY_ID_COLUMN, record.getCategoryId());
        contentValues.put(DbHelper.PRICE_COLUMN, record.getPrice());
        contentValues.put(DbHelper.ACCOUNT_ID_COLUMN, record.getAccountId());

        long id = db.insert(getTable(), null, contentValues);

        db.close();

        if (id == -1) {
            Log.d(TAG, "Couldn't create record : " + record);
            return null;
        } else {
            Record createdRecord = read(id);
            Log.d(TAG, "Created record : " + createdRecord);
            return createdRecord;
        }
    }

    @Nullable
    @Override
    public Record update(Record record) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DbHelper.TITLE_COLUMN, record.getTitle());
        contentValues.put(DbHelper.CATEGORY_ID_COLUMN, record.getCategoryId());
        contentValues.put(DbHelper.PRICE_COLUMN, record.getPrice());
        contentValues.put(DbHelper.ACCOUNT_ID_COLUMN, record.getAccountId());

        String[] args = {Long.valueOf(record.getId()).toString()};
        long rowsAffected = db.update(getTable(), contentValues, "id=?", args);

        db.close();

        if (rowsAffected == 0) {
            Log.d(TAG, "Couldn't update record : " + record);
            return null;
        } else {
            Record updatedRecord = read(record.getId());
            Log.d(TAG, "Updated record : " + updatedRecord);
            return updatedRecord;
        }
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