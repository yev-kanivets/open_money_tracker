package com.blogspot.e_kanivets.moneytracker;

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
    public static final String DEFAULT_ACCOUNT = "Default";
    public static final String DEFAULT_ACCOUNT_CURRENCY = "NON";

    public static final String TABLE_TRANSFERS = "transfers";
    public static final String FROM_ACCOUNT_ID_COLUMN = "from_account_id";
    public static final String TO_ACCOUNT_ID_COLUMN = "to_account_id";
    public static final String FROM_AMOUNT_COLUMN = "from_amount";
    public static final String TO_AMOUNT_COLUMN = "to_amount";
    public static final String CREATED_AT_COLUMN = "created_at";

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

            createAccountsTable(db);

            /* Add account_id column into the records table */
            db.execSQL("ALTER TABLE " + TABLE_RECORDS + " ADD COLUMN " + ACCOUNT_ID_COLUMN + " INTEGER;");

            createTransfersTable(db);

            /* Insert default account for all records from DB_VERSION = 1 */
            long id = insertDefaultAccount(db);

            /* Set the default account for all records from DB_VERSION = 1 */
            ContentValues contentValues = new ContentValues();
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

        createAccountsTable(db);

        createTransfersTable(db);

        insertDefaultAccount(db);
    }

    private void createAccountsTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_ACCOUNTS + "("
                + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + CREATED_AT_COLUMN + " INTEGER,"
                + TITLE_COLUMN + " TEXT,"
                + CUR_SUM_COLUMN + " INTEGER,"
                + CURRENCY_COLUMN + " TEXT );");
    }

    private void createTransfersTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_TRANSFERS + "("
                + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + TIME_COLUMN + " INTEGER,"
                + FROM_ACCOUNT_ID_COLUMN + " INTEGER,"
                + TO_ACCOUNT_ID_COLUMN + " INTEGER,"
                + FROM_AMOUNT_COLUMN + " INTEGER,"
                + TO_AMOUNT_COLUMN + " INTEGER);");
    }

    private long insertDefaultAccount(SQLiteDatabase db) {
        /* Insert default account for all records from DB_VERSION = 1 */
        ContentValues contentValues = new ContentValues();
        contentValues.put(TITLE_COLUMN, DEFAULT_ACCOUNT);
        contentValues.put(CUR_SUM_COLUMN, 0);
        contentValues.put(CURRENCY_COLUMN, DEFAULT_ACCOUNT_CURRENCY);
        contentValues.put(CREATED_AT_COLUMN, System.currentTimeMillis());

        return db.insert(TABLE_ACCOUNTS, null, contentValues);
    }
}