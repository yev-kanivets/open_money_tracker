package com.blogspot.e_kanivets.moneytracker.repo.data;

import android.content.ContentValues;
import android.database.Cursor;

import com.blogspot.e_kanivets.moneytracker.entity.data.Record;
import com.blogspot.e_kanivets.moneytracker.repo.DbHelper;
import com.blogspot.e_kanivets.moneytracker.repo.data.RecordRepo;

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
public class RecordRepoTest extends TestCase {
    private RecordRepo repo;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        DbHelper mock = Mockito.mock(DbHelper.class);
        repo = new RecordRepo(mock);
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        repo = null;
    }

    public void testGetTable() throws Exception {
        assertEquals(DbHelper.TABLE_RECORDS, repo.getTable());
    }

    public void testContentValues() throws Exception {
        Record record = new Record(1, 1, Record.TYPE_EXPENSE, "title", 1, 100, 1, "NON");

        ContentValues expected = new ContentValues();
        expected.put(DbHelper.TIME_COLUMN, 1L);
        expected.put(DbHelper.TYPE_COLUMN, Record.TYPE_EXPENSE);
        expected.put(DbHelper.TITLE_COLUMN, "title");
        expected.put(DbHelper.CATEGORY_ID_COLUMN, 1L);
        expected.put(DbHelper.PRICE_COLUMN, 100.0);
        expected.put(DbHelper.ACCOUNT_ID_COLUMN, 1L);
        expected.put(DbHelper.CURRENCY_COLUMN, "NON");

        ContentValues actual = repo.contentValues(record);

        assertEquals(expected, actual);

        assertNull(repo.contentValues(null));
    }

    public void testGetListFromCursor() throws Exception {
        assertEquals(new ArrayList<Record>(), repo.getListFromCursor(Mockito.mock(Cursor.class)));

        Cursor mockCursor = Mockito.mock(Cursor.class);
        Mockito.when(mockCursor.moveToFirst()).thenReturn(true);
        Mockito.when(mockCursor.getColumnIndex(DbHelper.ID_COLUMN)).thenReturn(1);
        Mockito.when(mockCursor.getColumnIndex(DbHelper.TIME_COLUMN)).thenReturn(2);
        Mockito.when(mockCursor.getColumnIndex(DbHelper.TYPE_COLUMN)).thenReturn(3);
        Mockito.when(mockCursor.getColumnIndex(DbHelper.TITLE_COLUMN)).thenReturn(4);
        Mockito.when(mockCursor.getColumnIndex(DbHelper.CATEGORY_ID_COLUMN)).thenReturn(5);
        Mockito.when(mockCursor.getColumnIndex(DbHelper.PRICE_COLUMN)).thenReturn(6);
        Mockito.when(mockCursor.getColumnIndex(DbHelper.ACCOUNT_ID_COLUMN)).thenReturn(7);
        Mockito.when(mockCursor.getColumnIndex(DbHelper.CURRENCY_COLUMN)).thenReturn(8);

        Mockito.when(mockCursor.getLong(1)).thenReturn(1L);
        Mockito.when(mockCursor.getLong(2)).thenReturn(1L);
        Mockito.when(mockCursor.getInt(3)).thenReturn(Record.TYPE_EXPENSE);
        Mockito.when(mockCursor.getString(4)).thenReturn("title");
        Mockito.when(mockCursor.getLong(5)).thenReturn(1L);
        Mockito.when(mockCursor.getInt(6)).thenReturn(100);
        Mockito.when(mockCursor.getLong(7)).thenReturn(1L);
        Mockito.when(mockCursor.getString(8)).thenReturn("NON");

        List<Record> expected = new ArrayList<>();
        expected.add(new Record(1, 1, Record.TYPE_EXPENSE, "title", 1, 100, 1, "NON"));

        assertEquals(expected, repo.getListFromCursor(mockCursor));

        assertEquals(new ArrayList<>(), repo.getListFromCursor(null));
    }
}