package com.blogspot.e_kanivets.moneytracker.repo;

import android.content.ContentValues;
import android.database.Cursor;

import com.blogspot.e_kanivets.moneytracker.entity.data.ExchangeRate;
import com.blogspot.e_kanivets.moneytracker.repo.data.ExchangeRateRepo;

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
public class ExchangeRateRepoTest extends TestCase {
    private ExchangeRateRepo repo;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        DbHelper mock = Mockito.mock(DbHelper.class);
        repo = new ExchangeRateRepo(mock);
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        repo = null;
    }

    public void testGetTable() throws Exception {
        assertEquals(DbHelper.TABLE_RATES, repo.getTable());
    }

    public void testContentValues() throws Exception {
        ExchangeRate rate = new ExchangeRate(1, 1, "NON", "USD", 100);

        ContentValues expected = new ContentValues();
        expected.put(DbHelper.CREATED_AT_COLUMN, 1L);
        expected.put(DbHelper.FROM_CURRENCY_COLUMN, "NON");
        expected.put(DbHelper.TO_CURRENCY_COLUMN, "USD");
        expected.put(DbHelper.AMOUNT_COLUMN, 100.0);

        ContentValues actual = repo.contentValues(rate);

        assertEquals(expected, actual);

        assertNull(repo.contentValues(null));
    }

    public void testGetListFromCursor() throws Exception {
        assertEquals(new ArrayList<ExchangeRate>(), repo.getListFromCursor(Mockito.mock(Cursor.class)));

        Cursor mockCursor = Mockito.mock(Cursor.class);
        Mockito.when(mockCursor.moveToFirst()).thenReturn(true);
        Mockito.when(mockCursor.getColumnIndex(DbHelper.ID_COLUMN)).thenReturn(1);
        Mockito.when(mockCursor.getColumnIndex(DbHelper.CREATED_AT_COLUMN)).thenReturn(2);
        Mockito.when(mockCursor.getColumnIndex(DbHelper.FROM_CURRENCY_COLUMN)).thenReturn(3);
        Mockito.when(mockCursor.getColumnIndex(DbHelper.TO_CURRENCY_COLUMN)).thenReturn(4);
        Mockito.when(mockCursor.getColumnIndex(DbHelper.AMOUNT_COLUMN)).thenReturn(5);

        Mockito.when(mockCursor.getLong(1)).thenReturn(1L);
        Mockito.when(mockCursor.getLong(2)).thenReturn(1L);
        Mockito.when(mockCursor.getString(3)).thenReturn("NON");
        Mockito.when(mockCursor.getString(4)).thenReturn("USD");
        Mockito.when(mockCursor.getDouble(5)).thenReturn(100.0);

        List<ExchangeRate> expected = new ArrayList<>();
        expected.add(new ExchangeRate(1, 1, "NON", "USD", 100.0));

        assertEquals(expected, repo.getListFromCursor(mockCursor));

        assertEquals(new ArrayList<>(), repo.getListFromCursor(null));
    }
}