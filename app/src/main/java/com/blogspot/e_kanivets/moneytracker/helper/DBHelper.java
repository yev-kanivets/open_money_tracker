package com.blogspot.e_kanivets.moneytracker.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.blogspot.e_kanivets.moneytracker.util.Constants;

/**
 * Created by eugene on 29/08/14.
 */
public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, Constants.DB_NAME, null, Constants.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + Constants.TABLE_RECORDS + "("
            + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "time INTEGER,"
            + "type INTEGER,"
            + "title TEXT,"
            + "category_id INTEGER,"
            + "price INTEGER" + ");");

        db.execSQL("CREATE TABLE " + Constants.TABLE_CATEGORIES + "("
            + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "name TEXT" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
