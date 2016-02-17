package com.blogspot.e_kanivets.moneytracker.repo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
public class CategoryRepo extends BaseRepo<Category> {
    @SuppressWarnings("unused")
    private static final String TAG = "CategoryRepo";

    public CategoryRepo(DbHelper dbHelper) {
        super(dbHelper);
    }

    @Override
    protected String getTable() {
        return DbHelper.TABLE_CATEGORIES;
    }

    @Nullable
    @Override
    public Category create(Category category) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DbHelper.NAME_COLUMN, category.getName());

        long id = db.insert(getTable(), null, contentValues);

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
    public Category update(Category category) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DbHelper.NAME_COLUMN, category.getName());

        String[] args = new String[]{Long.valueOf(category.getId()).toString()};
        long rowsAffected = db.update(getTable(), contentValues, "id=?", args);

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
    protected List<Category> getListFromCursor(Cursor cursor) {
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