package com.blogspot.e_kanivets.moneytracker.repo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.blogspot.e_kanivets.moneytracker.DbHelper;
import com.blogspot.e_kanivets.moneytracker.model.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link IRepo} implementation for {@link Category} entity.
 * Created on 2/15/16.
 *
 * @author Evgenii Kanivets
 */
public class CategoryRepo implements IRepo<Category> {
    @SuppressWarnings("unused")
    private static final String TAG = "CategoryRepo";

    private DbHelper dbHelper;

    public CategoryRepo(DbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    @Nullable
    @Override
    public Category create(Category category) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DbHelper.NAME_COLUMN, category.getName());

        long id = db.insert(DbHelper.TABLE_CATEGORIES, null, contentValues);

        db.close();

        if (id == -1) {
            Log.d(TAG, "Couldn't create category : " + category);
            return null;
        } else {
            Category createdCategory = read(id);
            Log.d(TAG, "Created account : " + createdCategory);
            return createdCategory;
        }
    }

    @Nullable
    @Override
    public Category read(long id) {
        List<Category> categoryList = readWithCondition("id=?", new String[]{Long.toString(id)});

        if (categoryList.size() == 1) return categoryList.get(0);
        else return null;
    }

    @Nullable
    @Override
    public Category update(Category category) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DbHelper.NAME_COLUMN, category.getName());

        String[] args = new String[]{Integer.valueOf(category.getId()).toString()};
        long rowsAffected = db.update(DbHelper.TABLE_CATEGORIES, contentValues, "id=?", args);

        db.close();

        if (rowsAffected == 0) {
            Log.d(TAG, "Couldn't update category : " + category);
            return null;
        } else {
            Category updatedCategory = read(category.getId());
            Log.d(TAG, "Updated category : " + updatedCategory);
            return updatedCategory;
        }
    }

    @Override
    public boolean delete(Category category) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String[] args = new String[]{Integer.toString(category.getId())};
        long rowsAffected = db.delete(DbHelper.TABLE_CATEGORIES, "id=?", args);

        db.close();

        Log.d(TAG, category + (rowsAffected == 0 ? " didn't " : " ") + "deleted");

        return rowsAffected != 0;
    }

    @NonNull
    @Override
    public List<Category> readAll() {
        return readWithCondition(null, null);
    }

    @NonNull
    @Override
    public List<Category> readWithCondition(String condition, String[] args) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        //Read categories table from db
        Cursor cursor = db.query(DbHelper.TABLE_CATEGORIES, null, condition, args, null, null, null);
        List<Category> categoryList = getCategoryListFromCursor(cursor);

        cursor.close();
        db.close();

        return categoryList;
    }

    private List<Category> getCategoryListFromCursor(Cursor cursor) {
        List<Category> categoryList = new ArrayList<>();

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

        return categoryList;
    }
}