package com.blogspot.e_kanivets.moneytracker.helper;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.blogspot.e_kanivets.moneytracker.model.Account;
import com.blogspot.e_kanivets.moneytracker.model.Category;
import com.blogspot.e_kanivets.moneytracker.model.Period;
import com.blogspot.e_kanivets.moneytracker.model.Record;
import com.blogspot.e_kanivets.moneytracker.util.MTApp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Observable;

/**
 * Helper class for Money Tracker application. Singleton.
 * Created on 01/09/14.
 *
 * @author Evgenii Kanivets
 */
public class MTHelper extends Observable {

    private static MTHelper mtHelper;

    private DbHelper dbHelper;

    private Period period;

    public static MTHelper getInstance() {
        if (mtHelper == null) {
            mtHelper = new MTHelper();
        }
        return mtHelper;
    }

    private MTHelper() {
        dbHelper = new DbHelper(MTApp.get());

        initPeriod();
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
                sb.append(getCategoryById(categoryId)).append(DELIMITER);
                sb.append(type == 0 ? price : -price);

                result.add(sb.toString());
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return result;
    }

    public void update() {
        //notify observers
        setChanged();
        notifyObservers();
    }

    public List<Category> getCategories() {
        List<Category> categoryList = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        //Read categories table from db
        Cursor cursor = db.query(DbHelper.TABLE_CATEGORIES, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            int idColIndex = cursor.getColumnIndex(DbHelper.ID_COLUMN);
            int nameColIndex = cursor.getColumnIndex(DbHelper.NAME_COLUMN);

            do {
                //Read a record from DB
                Category category = new Category(cursor.getInt(idColIndex),
                        cursor.getString(nameColIndex));

                //Add record to list
                categoryList.add(category);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return categoryList;
    }

    public List<Record> getRecords() {
        List<Record> recordList = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        //Form args to select only needed records according to period
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

    public void addRecord(long time, int type, String title, String category, int price, int accountId, int diff) {
        //Add category if it does not exist yet
        if (getCategoryIdByName(category) == -1) addCategory(category);
        int categoryId = getCategoryIdByName(category);

        //Add record to DB
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DbHelper.TIME_COLUMN, time);
        contentValues.put(DbHelper.TYPE_COLUMN, type);
        contentValues.put(DbHelper.TITLE_COLUMN, title);
        contentValues.put(DbHelper.CATEGORY_ID_COLUMN, categoryId);
        contentValues.put(DbHelper.PRICE_COLUMN, price);
        contentValues.put(DbHelper.ACCOUNT_ID_COLUMN, accountId);

        int id = (int) db.insert(DbHelper.TABLE_RECORDS, null, contentValues);

        updateAccountById(accountId, diff);

        db.close();

        update();
    }

    public void updateRecordById(int id, String title, String category, int price, int accountId, int diff) {
        //Add category if it does not exist yet
        if (getCategoryIdByName(category) == -1) {
            addCategory(category);
        }
        int categoryId = getCategoryIdByName(category);

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DbHelper.TITLE_COLUMN, title);
        contentValues.put(DbHelper.CATEGORY_ID_COLUMN, categoryId);
        contentValues.put(DbHelper.PRICE_COLUMN, price);
        contentValues.put(DbHelper.ACCOUNT_ID_COLUMN, accountId);

        db.update(DbHelper.TABLE_RECORDS, contentValues, "id=?", new String[]{Integer.valueOf(id).toString()});

        updateAccountById(accountId, diff);

        update();
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

        update();
    }

    /**
     * Deletes an account from DB and app cash. Uses the account id from @account.
     *
     * @param account to determine which account should be deleted.
     */
    public void deleteAccount(Account account) {
        // Delete the account from DB
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DbHelper.TABLE_ACCOUNTS, "id=?",
                new String[]{Integer.toString(account.getId())});
        db.close();

        update();
    }

    public void deleteRecordById(int id) {
        for (Record record : getRecords()) {
            if (record.getId() == id) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.delete(DbHelper.TABLE_RECORDS, "id=?",
                        new String[]{Integer.toString(id)});
                db.close();

                updateAccountById(record.getAccountId(), record.isIncome() ?
                        -record.getPrice() : record.getPrice());

                update();
                break;
            }
        }
    }

    public int addCategory(String name) {
        //Add category to DB
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);

        int id = (int) db.insert(DbHelper.TABLE_CATEGORIES, null, contentValues);

        db.close();

        update();

        return id;
    }

    public void deleteCategoryById(int id) {
        for (Category category : getCategories()) {
            if (category.getId() == id) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.delete(DbHelper.TABLE_CATEGORIES, "id=?", new String[]{Integer.toString(id)});

                update();

                break;
            }
        }
    }

    public String getCategoryById(int id) {
        for (Category category : getCategories()) {
            if (category.getId() == id) return category.getName();
        }

        return null;
    }

    public int getCategoryIdByName(String name) {
        for (Category category : getCategories()) {
            if (category.getName().equals(name)) return category.getId();
        }

        return -1;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    private void initPeriod() {
        // get today and clear time of day
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);

        // set first day of week
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());

        Date first = cal.getTime();

        cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);

        // set first day of week
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek() + 6);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);

        Date last = cal.getTime();

        period = new Period(first, last);
    }

    public String getFirstDay() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(period.getFirst());
    }

    public String getLastDay() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(period.getLast());
    }

    public void addAccount(String title, int curSum) {
        //Add account to DB
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DbHelper.TITLE_COLUMN, title);
        contentValues.put(DbHelper.CUR_SUM_COLUMN, curSum);

        int id = (int) db.insert(DbHelper.TABLE_ACCOUNTS, null, contentValues);

        db.close();

        update();
    }
}