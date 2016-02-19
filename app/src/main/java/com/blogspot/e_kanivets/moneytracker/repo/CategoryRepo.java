package com.blogspot.e_kanivets.moneytracker.repo;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;

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

    @NonNull
    @Override
    protected ContentValues contentValues(Category category) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbHelper.NAME_COLUMN, category.getName());

        return contentValues;
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