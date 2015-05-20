package com.blogspot.e_kanivets.moneytracker.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.blogspot.e_kanivets.moneytracker.util.Constants;

/**
 * DB Helper class
 * Created by eugene on 29/08/14.
 */
public class DBHelper extends SQLiteOpenHelper {

    public static final String ID_COLUMN = "id";
    public static final String TIME_COLUMN = "time";
    public static final String TYPE_COLUMN = "type";
    public static final String TITLE_COLUMN = "title";
    public static final String CATEGORY_ID_COLUMN = "category_id";
    public static final String PRICE_COLUMN = "price";
    public static final String NAME_COLUMN = "name";

    public DBHelper(Context context) {
        super(context, Constants.DB_NAME, null, Constants.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + Constants.TABLE_RECORDS + "("
            + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + TIME_COLUMN + " INTEGER,"
            + TYPE_COLUMN + " INTEGER,"
            + TITLE_COLUMN + " TEXT,"
            + CATEGORY_ID_COLUMN + " INTEGER,"
            + PRICE_COLUMN + " INTEGER" + ");");

        db.execSQL("CREATE TABLE " + Constants.TABLE_CATEGORIES + "("
            + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + NAME_COLUMN + " TEXT" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
