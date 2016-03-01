package com.blogspot.e_kanivets.moneytracker.repo;

import android.content.ContentValues;
import android.database.Cursor;

import com.blogspot.e_kanivets.moneytracker.DbHelper;
import com.blogspot.e_kanivets.moneytracker.entity.Category;

import junit.framework.TestCase;

import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

/**
 * Android Test case.
 * Created on 3/1/16.
 *
 * @author Evgenii Kanivets
 */
public class CategoryRepoTest extends TestCase {
    private CategoryRepo repo;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        DbHelper mock = Mockito.mock(DbHelper.class);
        repo = new CategoryRepo(mock);
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        repo = null;
    }

    public void testGetTable() throws Exception {
        assertEquals(DbHelper.TABLE_CATEGORIES, repo.getTable());
    }

    public void testContentValues() throws Exception {
        Category category = new Category(1, "category");

        ContentValues expected = new ContentValues();
        expected.put(DbHelper.NAME_COLUMN, "category");

        ContentValues actual = repo.contentValues(category);

        assertEquals(expected, actual);

        assertNull(repo.contentValues(null));
    }

    public void testGetListFromCursor() throws Exception {
        assertEquals(new ArrayList<Category>(), repo.getListFromCursor(Mockito.mock(Cursor.class)));

        Cursor mockCursor = Mockito.mock(Cursor.class);
        Mockito.when(mockCursor.moveToFirst()).thenReturn(true);
        Mockito.when(mockCursor.getColumnIndex(DbHelper.ID_COLUMN)).thenReturn(1);
        Mockito.when(mockCursor.getColumnIndex(DbHelper.NAME_COLUMN)).thenReturn(2);

        Mockito.when(mockCursor.getLong(1)).thenReturn(1L);
        Mockito.when(mockCursor.getString(2)).thenReturn("category");

        List<Category> expected = new ArrayList<>();
        expected.add(new Category(1, "category"));

        assertEquals(expected, repo.getListFromCursor(mockCursor));

        assertEquals(new ArrayList<>(), repo.getListFromCursor(null));
    }
}