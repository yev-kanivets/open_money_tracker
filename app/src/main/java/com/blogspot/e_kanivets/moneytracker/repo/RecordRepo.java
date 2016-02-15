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
public class RecordRepo implements IRepo<Record> {
    @SuppressWarnings("unused")
    private static final String TAG = "RecordRepo";

    private final DbHelper dbHelper;
    private final AccountController accountController;
    private final CategoryController categoryController;

    public RecordRepo(DbHelper dbHelper, AccountController accountController, CategoryController categoryController) {
        this.dbHelper = dbHelper;
        this.accountController = accountController;
        this.categoryController = categoryController;
    }

    @Nullable
    @Override
    public Record create(Record record) {
        record.setCategoryId(categoryController.readOrCreate(record.getCategory()).getId());

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DbHelper.TIME_COLUMN, record.getTime());
        contentValues.put(DbHelper.TYPE_COLUMN, record.getType());
        contentValues.put(DbHelper.TITLE_COLUMN, record.getTitle());
        contentValues.put(DbHelper.CATEGORY_ID_COLUMN, record.getCategoryId());
        contentValues.put(DbHelper.PRICE_COLUMN, record.getPrice());
        contentValues.put(DbHelper.ACCOUNT_ID_COLUMN, record.getAccountId());

        long id = db.insert(DbHelper.TABLE_RECORDS, null, contentValues);

        db.close();

        if (id == -1) {
            Log.d(TAG, "Couldn't create record : " + record);
            return null;
        } else {
            Record createdRecord = read(id);
            accountController.recordAdded(createdRecord);
            Log.d(TAG, "Created record : " + createdRecord);

            return createdRecord;
        }
    }

    @Nullable
    @Override
    public Record read(long id) {
        List<Record> recordList = readWithCondition("id=?", new String[]{Long.toString(id)});

        if (recordList.size() == 1) return recordList.get(0);
        else return null;
    }

    @Nullable
    @Override
    public Record update(Record record) {
        Record oldRecord = read(record.getId());

        int categoryId = categoryController.readOrCreate(record.getCategory()).getId();

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DbHelper.TITLE_COLUMN, record.getTitle());
        contentValues.put(DbHelper.CATEGORY_ID_COLUMN, categoryId);
        contentValues.put(DbHelper.PRICE_COLUMN, record.getPrice());
        contentValues.put(DbHelper.ACCOUNT_ID_COLUMN, record.getAccountId());

        String[] args = {Integer.valueOf(record.getId()).toString()};
        long rowsAffected = db.update(DbHelper.TABLE_RECORDS, contentValues, "id=?", args);

        db.close();

        if (rowsAffected == 0) {
            Log.d(TAG, "Couldn't update record : " + record);
            return null;
        } else {
            Record updatedRecord = read(record.getId());
            accountController.recordUpdated(oldRecord, updatedRecord);

            Log.d(TAG, "Updated record : " + updatedRecord);
            return updatedRecord;
        }
    }

    @Override
    public boolean delete(Record record) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String[] args = new String[]{Integer.toString(record.getId())};
        long rowsAffected = db.delete(DbHelper.TABLE_RECORDS, "id=?", args);
        db.close();

        accountController.recordDeleted(record);

        Log.d(TAG, record + (rowsAffected == 0 ? " didn't " : " ") + "deleted");

        return rowsAffected != 0;
    }

    @NonNull
    @Override
    public List<Record> readAll() {
        return readWithCondition(null, null);
    }

    @NonNull
    @Override
    public List<Record> readWithCondition(String condition, String[] args) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        //Read records table from db
        Cursor cursor = db.query(DbHelper.TABLE_RECORDS, null, condition, args, null, null, null);
        List<Record> recordList = getRecordListFromCursor(cursor);

        cursor.close();
        db.close();

        return recordList;
    }

    private List<Record> getRecordListFromCursor(Cursor cursor) {
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