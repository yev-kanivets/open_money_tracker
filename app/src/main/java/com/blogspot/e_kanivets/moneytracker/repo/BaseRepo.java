package com.blogspot.e_kanivets.moneytracker.repo;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.blogspot.e_kanivets.moneytracker.DbHelper;
import com.blogspot.e_kanivets.moneytracker.model.IEntity;

import java.util.List;

/**
 * Base implementation of {@link IRepo}.
 * Created on 2/15/16.
 *
 * @author Evgenii Kanivets
 */
public abstract class BaseRepo<T extends IEntity> implements IRepo<T> {
    private static final String TAG = "BaseRepo";

    protected DbHelper dbHelper;

    public BaseRepo(DbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    abstract String getTable();

    abstract List<T> getListFromCursor(Cursor cursor);

    @Nullable
    @Override
    public T read(long id) {
        List<T> list = readWithCondition("id=?", new String[]{Long.toString(id)});

        if (list.size() == 1) return list.get(0);
        else return null;
    }

    @NonNull
    @Override
    public List<T> readAll() {
        return readWithCondition(null, null);
    }

    @Override
    public boolean delete(T instance) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String[] args = new String[]{Long.toString(instance.getId())};
        long rowsAffected = db.delete(getTable(), "id=?", args);

        db.close();

        Log.d(TAG, instance + (rowsAffected == 0 ? " didn't " : " ") + "deleted");

        return rowsAffected != 0;
    }

    @NonNull
    @Override
    public List<T> readWithCondition(String condition, String[] args) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Read accounts table from db
        Cursor cursor = db.query(getTable(), null, condition, args, null, null, null);
        List<T> accountList = getListFromCursor(cursor);

        cursor.close();
        db.close();

        return accountList;
    }
}