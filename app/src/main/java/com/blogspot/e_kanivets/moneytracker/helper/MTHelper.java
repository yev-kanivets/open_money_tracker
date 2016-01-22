package com.blogspot.e_kanivets.moneytracker.helper;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.blogspot.e_kanivets.moneytracker.model.Account;
import com.blogspot.e_kanivets.moneytracker.model.Category;
import com.blogspot.e_kanivets.moneytracker.model.Period;
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
public class MtHelper extends Observable {

    private static MtHelper mtHelper;

    private DbHelper dbHelper;

    private Period period;

    public static MtHelper getInstance() {
        if (mtHelper == null) {
            mtHelper = new MtHelper();
        }
        return mtHelper;
    }

    private MtHelper() {
        dbHelper = new DbHelper(MTApp.get());

        initPeriod();
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

        update();
    }

    public void deleteAccount(Account account) {
        // Delete the account from DB
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DbHelper.TABLE_ACCOUNTS, "id=?",
                new String[]{Integer.toString(account.getId())});
        db.close();

        update();
    }

    public int addCategory(String name) {
        //Add category to DB
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DbHelper.NAME_COLUMN, name);

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