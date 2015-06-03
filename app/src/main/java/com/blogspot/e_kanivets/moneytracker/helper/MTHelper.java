package com.blogspot.e_kanivets.moneytracker.helper;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.blogspot.e_kanivets.moneytracker.model.Category;
import com.blogspot.e_kanivets.moneytracker.model.Period;
import com.blogspot.e_kanivets.moneytracker.model.Record;
import com.blogspot.e_kanivets.moneytracker.util.Constants;
import com.blogspot.e_kanivets.moneytracker.util.MTApp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Observable;

/**
 * Helper class for Money Tracker application. Singleton.
 * Created by eugene on 01/09/14.
 */
public class MTHelper extends Observable {

    private static MTHelper mtHelper;

    private DBHelper dbHelper;

    private List<Category> categories;
    private List<Record> records;

    private Period period;

    public static MTHelper getInstance() {
        if (mtHelper == null) {
            mtHelper = new MTHelper();
        }
        return mtHelper;
    }

    private MTHelper() {
        dbHelper = new DBHelper(MTApp.get());

        initPeriod();
        categories = new ArrayList<>();
        records = new ArrayList<>();
    }

    public void initialize() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        //Read categories table from db
        Cursor cursor = db.query(DBHelper.TABLE_CATEGORIES, null, null, null, null, null, null);
        categories.clear();

        if (cursor.moveToFirst()) {
            int idColIndex = cursor.getColumnIndex("id");
            int nameColIndex = cursor.getColumnIndex("name");

            do {
                //Read a record from DB
                Category category = new Category(cursor.getInt(idColIndex),
                        cursor.getString(nameColIndex));
                /*Log.d(Constants.TAG, "id = " + cursor.getInt(idColIndex) +
                        " name = " + cursor.getString(nameColIndex));*/

                //Add record to list
                categories.add(category);
            } while (cursor.moveToNext());
        }

        cursor.close();

        //Form args to select only needed records according to period
        String[] args = new String[]{Long.toString(period.getFirst().getTime()),
                Long.toString(period.getLast().getTime())};

        //Read records table from db
        cursor = db.query(DBHelper.TABLE_RECORDS, null, "time BETWEEN ? AND ?", args, null, null, null);
        records.clear();

        if (cursor.moveToFirst()) {
            //Get indexes of columns
            int idColIndex = cursor.getColumnIndex("id");
            int timeColIndex = cursor.getColumnIndex("time");
            int typeColIndex = cursor.getColumnIndex("type");
            int titleColIndex = cursor.getColumnIndex("title");
            int categoryColIndex = cursor.getColumnIndex("category_id");
            int priceColIndex = cursor.getColumnIndex("price");

            do {
                //Read a record from DB
                Record record = new Record(cursor.getInt(idColIndex),
                        cursor.getLong(timeColIndex),
                        cursor.getInt(typeColIndex),
                        cursor.getString(titleColIndex),
                        cursor.getInt(categoryColIndex),
                        cursor.getInt(priceColIndex));
                /*Log.d(Constants.TAG, "id = " + cursor.getInt(idColIndex) +
                    " time = " + cursor.getLong(timeColIndex) +
                    " type = " + cursor.getInt(typeColIndex) +
                    " title = " + cursor.getString(titleColIndex) +
                    " category = " + cursor.getInt(categoryColIndex) +
                    " price = " + cursor.getInt(priceColIndex));*/

                //Add record to list
                records.add(record);
            } while (cursor.moveToNext());
        }

        db.close();
    }

    public List<String> getRecordsForExport(long fromDate, long toDate) {
        final String DELIMITER = ";";
        List<String> result = new ArrayList<>();

        /* First of all add a header */
        @SuppressWarnings("StringBufferReplaceableByString")
        StringBuilder sb = new StringBuilder();
        sb.append(DBHelper.ID_COLUMN).append(DELIMITER);
        sb.append(DBHelper.TIME_COLUMN).append(DELIMITER);
        sb.append(DBHelper.TITLE_COLUMN).append(DELIMITER);
        sb.append("category").append(DELIMITER);
        sb.append(DBHelper.PRICE_COLUMN);

        result.add(sb.toString());

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        //Form args to select only needed records according to period
        String[] args = new String[]{Long.toString(fromDate),
                Long.toString(toDate)};

        //Read records table from db
        Cursor cursor = db.query(DBHelper.TABLE_RECORDS, null, "time BETWEEN ? AND ?", args, null, null, null);

        if (cursor.moveToFirst()) {
            //Get indexes of columns
            int idColIndex = cursor.getColumnIndex("id");
            int timeColIndex = cursor.getColumnIndex("time");
            int typeColIndex = cursor.getColumnIndex("type");
            int titleColIndex = cursor.getColumnIndex("title");
            int categoryColIndex = cursor.getColumnIndex("category_id");
            int priceColIndex = cursor.getColumnIndex("price");

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
        initialize();

        //notify observers
        setChanged();
        notifyObservers();
    }

    public List<Record> getRecords() {
        return records;
    }

    public void addRecord(long time, int type, String title, String category, int price) {
        //Add category if it does not exist yet
        if (getCategoryIdByName(category) == -1) {
            addCategory(category);
        }
        int categoryId = getCategoryIdByName(category);

        //Add record to DB
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("time", time);
        contentValues.put("type", type);
        contentValues.put("title", title);
        contentValues.put("category_id", categoryId);
        contentValues.put("price", price);

        int id = (int) db.insert(DBHelper.TABLE_RECORDS, null, contentValues);

        db.close();

        //Add record to app list
        records.add(new Record(id, time, type, title, categoryId, price));

        //notify observers
        setChanged();
        notifyObservers();
    }

    public void updateRecordById(int id, String title, String category, int price) {
        //Add category if it does not exist yet
        if (getCategoryIdByName(category) == -1) {
            addCategory(category);
        }
        int categoryId = getCategoryIdByName(category);

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("title", title);
        contentValues.put("category_id", categoryId);
        contentValues.put("price", price);

        db.update(DBHelper.TABLE_RECORDS, contentValues, "id=?", new String[]{Integer.valueOf(id).toString()});

        //Change particular record
        for (Record record : records) {
            if (record.getId() == id) {
                record.setTitle(title);
                record.setCategoryId(categoryId);
                record.setCategory(category);
                record.setPrice(price);
            }
        }

        //notify observers
        setChanged();
        notifyObservers();
    }

    public void deleteRecordById(int id) {
        for (Record record : records) {
            if (record.getId() == id) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.delete(DBHelper.TABLE_RECORDS, "id=?",
                        new String[]{Integer.toString(id)});
                db.close();

                records.remove(record);

                //notify observers
                setChanged();
                notifyObservers();

                break;
            }
        }
    }

    public int addCategory(String name) {
        //Add category to DB
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);

        int id = (int) db.insert(DBHelper.TABLE_CATEGORIES, null, contentValues);

        db.close();

        //Add category to app list
        categories.add(new Category(id, name));

        return id;
    }

    public void deleteCategoryById(int id) {
        for (Category category : categories) {
            if (category.getId() == id) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.delete(DBHelper.TABLE_CATEGORIES, "id=?",
                        new String[]{Integer.toString(id)});

                categories.remove(category);

                break;
            }
        }
    }

    public String getCategoryById(int id) {
        for (Category category : categories) {
            if (category.getId() == id) return category.getName();
        }

        return null;
    }

    public int getCategoryIdByName(String name) {
        for (Category category : categories) {
            //Log.d(Constants.TAG, name + " " + category.getName() + " " + category.getName().equals(name));
            if (category.getName().equals(name)) {
                return category.getId();
            }
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
}
