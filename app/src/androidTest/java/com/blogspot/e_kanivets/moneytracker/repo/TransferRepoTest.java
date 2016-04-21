package com.blogspot.e_kanivets.moneytracker.repo;

import android.content.ContentValues;
import android.database.Cursor;

import com.blogspot.e_kanivets.moneytracker.entity.data.Transfer;
import com.blogspot.e_kanivets.moneytracker.repo.data.TransferRepo;

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
public class TransferRepoTest extends TestCase {
    private TransferRepo repo;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        DbHelper mock = Mockito.mock(DbHelper.class);
        repo = new TransferRepo(mock);
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        repo = null;
    }

    public void testGetTable() throws Exception {
        assertEquals(DbHelper.TABLE_TRANSFERS, repo.getTable());
    }

    public void testContentValues() throws Exception {
        Transfer transfer = new Transfer(1, 1, 1, 2, 100, 200);

        ContentValues expected = new ContentValues();
        expected.put(DbHelper.TIME_COLUMN, 1L);
        expected.put(DbHelper.FROM_ACCOUNT_ID_COLUMN, 1L);
        expected.put(DbHelper.TO_ACCOUNT_ID_COLUMN, 2L);
        expected.put(DbHelper.FROM_AMOUNT_COLUMN, 100);
        expected.put(DbHelper.TO_AMOUNT_COLUMN, 200);

        ContentValues actual = repo.contentValues(transfer);

        assertEquals(expected, actual);

        assertNull(repo.contentValues(null));
    }

    public void testGetListFromCursor() throws Exception {
        assertEquals(new ArrayList<Transfer>(), repo.getListFromCursor(Mockito.mock(Cursor.class)));

        Cursor mockCursor = Mockito.mock(Cursor.class);
        Mockito.when(mockCursor.moveToFirst()).thenReturn(true);
        Mockito.when(mockCursor.getColumnIndex(DbHelper.ID_COLUMN)).thenReturn(1);
        Mockito.when(mockCursor.getColumnIndex(DbHelper.TIME_COLUMN)).thenReturn(2);
        Mockito.when(mockCursor.getColumnIndex(DbHelper.FROM_ACCOUNT_ID_COLUMN)).thenReturn(3);
        Mockito.when(mockCursor.getColumnIndex(DbHelper.TO_ACCOUNT_ID_COLUMN)).thenReturn(4);
        Mockito.when(mockCursor.getColumnIndex(DbHelper.FROM_AMOUNT_COLUMN)).thenReturn(5);
        Mockito.when(mockCursor.getColumnIndex(DbHelper.TO_AMOUNT_COLUMN)).thenReturn(6);

        Mockito.when(mockCursor.getLong(1)).thenReturn(1L);
        Mockito.when(mockCursor.getLong(2)).thenReturn(1L);
        Mockito.when(mockCursor.getLong(3)).thenReturn(1L);
        Mockito.when(mockCursor.getLong(4)).thenReturn(2L);
        Mockito.when(mockCursor.getInt(5)).thenReturn(100);
        Mockito.when(mockCursor.getInt(6)).thenReturn(200);

        List<Transfer> expected = new ArrayList<>();
        expected.add(new Transfer(1, 1, 1, 2, 100, 200));

        assertEquals(expected, repo.getListFromCursor(mockCursor));

        assertEquals(new ArrayList<>(), repo.getListFromCursor(null));
    }
}