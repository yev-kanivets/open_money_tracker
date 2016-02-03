package com.blogspot.e_kanivets.moneytracker.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * DB Helper class.
 * Created on 29/08/14.
 *
 * @author Evgenii Kanivets
 */
public class DbHelper extends SQLiteOpenHelper {

    /* DB_VERSION = 1 */
    public static final String DB_NAME = "database";
    public static final int DB_VERSION = 2;
    public static final String TABLE_RECORDS = "records";
    public static final String TABLE_CATEGORIES = "categories";

    public static final String ID_COLUMN = "id";
    public static final String TIME_COLUMN = "time";
    public static final String TYPE_COLUMN = "type";
    public static final String TITLE_COLUMN = "title";
    public static final String CATEGORY_ID_COLUMN = "category_id";
    public static final String PRICE_COLUMN = "price";
    public static final String NAME_COLUMN = "name";

    /* DB_VERSION = 2 */
    public static final String TABLE_ACCOUNTS = "accounts";
    public static final String CURRENCY_COLUMN = "currency";

    public static final String ACCOUNT_ID_COLUMN = "account_id";
    public static final String CUR_SUM_COLUMN = "cur_sum";
    public static final String DEFAULT_ACCOUNT = "default_account";

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //createDbVersion1(db);
        createDbVersion2(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion == 1 && newVersion == 2) {
            db.beginTransaction();

            /* Create accounts table */
            db.execSQL("CREATE TABLE " + TABLE_ACCOUNTS + "("
                    + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + TITLE_COLUMN + " TEXT,"
                    + CUR_SUM_COLUMN + " INTEGER);");

            /* Add account_id column into the records table */
            db.execSQL("ALTER TABLE " + TABLE_RECORDS + " ADD COLUMN " + ACCOUNT_ID_COLUMN + " INTEGER;");

            /* Insert default account for all records from DB_VERSION = 1 */
            ContentValues contentValues = new ContentValues();
            contentValues.put(TITLE_COLUMN, DEFAULT_ACCOUNT);
            contentValues.put(CUR_SUM_COLUMN, 0);
            int id = (int) db.insert(TABLE_ACCOUNTS, null, contentValues);

            /* Set the default account for all records from DB_VERSION = 1 */
            contentValues = new ContentValues();
            contentValues.put(ACCOUNT_ID_COLUMN, id);
            db.update(DbHelper.TABLE_RECORDS, contentValues, null, null);

            db.setTransactionSuccessful();

            db.endTransaction();
        }
    }

    @SuppressWarnings("unused")
    private void createDbVersion1(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_RECORDS + "("
                + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + TIME_COLUMN + " INTEGER,"
                + TYPE_COLUMN + " INTEGER,"
                + TITLE_COLUMN + " TEXT,"
                + CATEGORY_ID_COLUMN + " INTEGER,"
                + PRICE_COLUMN + " INTEGER" + ");");

        db.execSQL("CREATE TABLE " + TABLE_CATEGORIES + "("
                + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + NAME_COLUMN + " TEXT" + ");");
    }

    private void createDbVersion2(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_RECORDS + "("
                + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + TIME_COLUMN + " INTEGER,"
                + TYPE_COLUMN + " INTEGER,"
                + TITLE_COLUMN + " TEXT,"
                + CATEGORY_ID_COLUMN + " INTEGER,"
                + PRICE_COLUMN + " INTEGER,"
                + ACCOUNT_ID_COLUMN + " INTEGER" + ");");

        db.execSQL("CREATE TABLE " + TABLE_CATEGORIES + "("
                + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + NAME_COLUMN + " TEXT" + ");");

        db.execSQL("CREATE TABLE " + TABLE_ACCOUNTS + "("
                + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + TITLE_COLUMN + " TEXT,"
                + CUR_SUM_COLUMN + " INTEGER,"
                + CURRENCY_COLUMN + " TEXT );");

        /* Insert default account for all records from DB_VERSION = 1 */
        ContentValues contentValues = new ContentValues();
        contentValues.put(TITLE_COLUMN, DEFAULT_ACCOUNT);
        contentValues.put(CUR_SUM_COLUMN, 0);

        db.insert(TABLE_ACCOUNTS, null, contentValues);
    }
}
