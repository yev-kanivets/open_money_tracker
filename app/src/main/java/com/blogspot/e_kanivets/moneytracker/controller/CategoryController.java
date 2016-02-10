package com.blogspot.e_kanivets.moneytracker.controller;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.blogspot.e_kanivets.moneytracker.helper.DbHelper;
import com.blogspot.e_kanivets.moneytracker.model.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller class to encapsulate category handling logic.
 * Created on 1/23/16.
 *
 * @author Evgenii Kanivets
 */
public class CategoryController {
    private static final String TAG = "CategoryController";

    private DbHelper dbHelper;

    public CategoryController(DbHelper dbHelper) {
        this.dbHelper = dbHelper;
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

    public int addCategory(String name) {
        //Add category to DB
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DbHelper.NAME_COLUMN, name);

        int id = (int) db.insert(DbHelper.TABLE_CATEGORIES, null, contentValues);
        Log.d(TAG, "created category with id = " + id);

        db.close();

        return id;
    }

    public void deleteCategoryById(int id) {
        for (Category category : getCategories()) {
            if (category.getId() == id) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.delete(DbHelper.TABLE_CATEGORIES, "id=?", new String[]{Integer.toString(id)});
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
}