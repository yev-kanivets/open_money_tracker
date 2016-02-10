package com.blogspot.e_kanivets.moneytracker.controller;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.blogspot.e_kanivets.moneytracker.helper.DbHelper;
import com.blogspot.e_kanivets.moneytracker.model.Period;
import com.blogspot.e_kanivets.moneytracker.model.Record;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller class to encapsulate record handling logic.
 * Created on 1/22/16.
 *
 * @author Evgenii Kanivets
 */
public class RecordController {
    private DbHelper dbHelper;
    private AccountController accountController;
    private final CategoryController categoryController;

    public RecordController(DbHelper dbHelper) {
        this.dbHelper = dbHelper;
        this.accountController = new AccountController(dbHelper);
        categoryController = new CategoryController(dbHelper);
    }

    public Record read(long id) {
        Record record = null;

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        //Read records table from db
        Cursor cursor = db.query(DbHelper.TABLE_RECORDS, null, "id=?",
                new String[]{Long.toString(id)}, null, null, null);

        if (cursor.moveToFirst()) {
            //Get indexes of columns
            int idColIndex = cursor.getColumnIndex(DbHelper.ID_COLUMN);
            int timeColIndex = cursor.getColumnIndex(DbHelper.TIME_COLUMN);
            int typeColIndex = cursor.getColumnIndex(DbHelper.TYPE_COLUMN);
            int titleColIndex = cursor.getColumnIndex(DbHelper.TITLE_COLUMN);
            int categoryColIndex = cursor.getColumnIndex(DbHelper.CATEGORY_ID_COLUMN);
            int priceColIndex = cursor.getColumnIndex(DbHelper.PRICE_COLUMN);
            int accountIdColIndex = cursor.getColumnIndex(DbHelper.ACCOUNT_ID_COLUMN);

            do {
                //Read a record from DB
                record = new Record(cursor.getInt(idColIndex),
                        cursor.getLong(timeColIndex),
                        cursor.getInt(typeColIndex),
                        cursor.getString(titleColIndex),
                        cursor.getInt(categoryColIndex),
                        cursor.getInt(priceColIndex),
                        cursor.getInt(accountIdColIndex));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return record;
    }

    public List<Record> getRecords(Period period) {
        List<Record> recordList = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Form args to select only needed records according to period
        String[] args = new String[]{Long.toString(period.getFirst().getTime()),
                Long.toString(period.getLast().getTime())};

        //Read records table from db
        Cursor cursor = db.query(DbHelper.TABLE_RECORDS, null, "time BETWEEN ? AND ?", args, null, null, null);

        if (cursor.moveToFirst()) {
            //Get indexes of columns
            int idColIndex = cursor.getColumnIndex(DbHelper.ID_COLUMN);
            int timeColIndex = cursor.getColumnIndex(DbHelper.TIME_COLUMN);
            int typeColIndex = cursor.getColumnIndex(DbHelper.TYPE_COLUMN);
            int titleColIndex = cursor.getColumnIndex(DbHelper.TITLE_COLUMN);
            int categoryColIndex = cursor.getColumnIndex(DbHelper.CATEGORY_ID_COLUMN);
            int priceColIndex = cursor.getColumnIndex(DbHelper.PRICE_COLUMN);
            int accountIdColIndex = cursor.getColumnIndex(DbHelper.ACCOUNT_ID_COLUMN);

            do {
                //Read a record from DB
                Record record = new Record(cursor.getInt(idColIndex),
                        cursor.getLong(timeColIndex),
                        cursor.getInt(typeColIndex),
                        cursor.getString(titleColIndex),
                        cursor.getInt(categoryColIndex),
                        cursor.getInt(priceColIndex),
                        cursor.getInt(accountIdColIndex));

                //Add record to list
                recordList.add(record);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return recordList;
    }

    public void addRecord(Record record) {
        //Add category if it does not exist yet
        if (categoryController.getCategoryIdByName(record.getCategory()) == -1) {
            categoryController.addCategory(record.getCategory());
            record.setCategoryId(categoryController.getCategoryIdByName(record.getCategory()));
        }

        //Add record to DB
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DbHelper.TIME_COLUMN, record.getTime());
        contentValues.put(DbHelper.TYPE_COLUMN, record.getType());
        contentValues.put(DbHelper.TITLE_COLUMN, record.getTitle());
        contentValues.put(DbHelper.CATEGORY_ID_COLUMN, record.getCategoryId());
        contentValues.put(DbHelper.PRICE_COLUMN, record.getPrice());
        contentValues.put(DbHelper.ACCOUNT_ID_COLUMN, record.getAccountId());

        db.insert(DbHelper.TABLE_RECORDS, null, contentValues);

        db.close();

        accountController.recordAdded(record);
    }

    public void updateRecordById(int id, String title, String category, int price, int accountId) {
        //Add category if it does not exist yet
        if (categoryController.getCategoryIdByName(category) == -1) {
            categoryController.addCategory(category);
        }

        Record oldRecord = read(id);

        int categoryId = categoryController.getCategoryIdByName(category);

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DbHelper.TITLE_COLUMN, title);
        contentValues.put(DbHelper.CATEGORY_ID_COLUMN, categoryId);
        contentValues.put(DbHelper.PRICE_COLUMN, price);
        contentValues.put(DbHelper.ACCOUNT_ID_COLUMN, accountId);

        db.update(DbHelper.TABLE_RECORDS, contentValues, "id=?", new String[]{Integer.valueOf(id).toString()});

        db.close();

        Record newRecord = read(id);

        accountController.recordUpdated(oldRecord, newRecord);
    }

    public void deleteRecord(Record record) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DbHelper.TABLE_RECORDS, "id=?",
                new String[]{Integer.toString(record.getId())});
        db.close();

        accountController.recordDeleted(record);
    }

    public List<String> getRecordsForExport(long fromDate, long toDate) {
        final String DELIMITER = ";";
        List<String> result = new ArrayList<>();

        /* First of all add a header */
        @SuppressWarnings("StringBufferReplaceableByString")
        StringBuilder sb = new StringBuilder();
        sb.append(DbHelper.ID_COLUMN).append(DELIMITER);
        sb.append(DbHelper.TIME_COLUMN).append(DELIMITER);
        sb.append(DbHelper.TITLE_COLUMN).append(DELIMITER);
        sb.append(DbHelper.CATEGORY_ID_COLUMN).append(DELIMITER);
        sb.append(DbHelper.PRICE_COLUMN);

        result.add(sb.toString());

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        //Form args to select only needed records according to period
        String[] args = new String[]{Long.toString(fromDate),
                Long.toString(toDate)};

        //Read records table from db
        Cursor cursor = db.query(DbHelper.TABLE_RECORDS, null, "time BETWEEN ? AND ?", args, null, null, null);

        if (cursor.moveToFirst()) {
            //Get indexes of columns
            int idColIndex = cursor.getColumnIndex(DbHelper.ID_COLUMN);
            int timeColIndex = cursor.getColumnIndex(DbHelper.TIME_COLUMN);
            int typeColIndex = cursor.getColumnIndex(DbHelper.TYPE_COLUMN);
            int titleColIndex = cursor.getColumnIndex(DbHelper.TITLE_COLUMN);
            int categoryColIndex = cursor.getColumnIndex(DbHelper.CATEGORY_ID_COLUMN);
            int priceColIndex = cursor.getColumnIndex(DbHelper.PRICE_COLUMN);

            do {
                //Read a record from DB
                int id = cursor.getInt(idColIndex);
                long time = cursor.getLong(timeColIndex);
                int type = cursor.getInt(typeColIndex);
                String title = cursor.getString(titleColIndex);
                int categoryId = cursor.getInt(categoryColIndex);
                int price = cursor.getInt(priceColIndex);

                sb = new StringBuilder();
                sb.append(id).append(DELIMITER);
                sb.append(time).append(DELIMITER);
                sb.append(title).append(DELIMITER);
                sb.append(categoryController.getCategoryById(categoryId)).append(DELIMITER);
                sb.append(type == 0 ? price : -price);

                result.add(sb.toString());
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return result;
    }
}