package com.blogspot.e_kanivets.moneytracker.repo.data;

import android.content.ContentValues;
import android.database.Cursor;

import com.blogspot.e_kanivets.moneytracker.entity.data.Account;
import com.blogspot.e_kanivets.moneytracker.repo.DbHelper;

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
public class AccountRepoTest extends TestCase {
    private AccountRepo repo;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        DbHelper mock = Mockito.mock(DbHelper.class);
        repo = new AccountRepo(mock);
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        repo = null;
    }

    public void testGetTable() throws Exception {
        assertEquals(DbHelper.TABLE_ACCOUNTS, repo.getTable());
    }

    public void testContentValues() throws Exception {
        Account account = new Account(-1, "title1", 100, "NON", 30);

        ContentValues expected = new ContentValues();
        expected.put(DbHelper.TITLE_COLUMN, "title1");
        expected.put(DbHelper.CUR_SUM_COLUMN, 100);
        expected.put(DbHelper.CURRENCY_COLUMN, "NON");
        expected.put(DbHelper.DECIMALS_COLUMN, 30);

        ContentValues actual = repo.contentValues(account);

        assertEquals(expected, actual);

        assertNull(repo.contentValues(null));
    }

    public void testGetListFromCursor() throws Exception {
        assertEquals(new ArrayList<Account>(), repo.getListFromCursor(Mockito.mock(Cursor.class)));

        Cursor mockCursor = Mockito.mock(Cursor.class);
        Mockito.when(mockCursor.moveToFirst()).thenReturn(true);
        Mockito.when(mockCursor.getColumnIndex(DbHelper.ID_COLUMN)).thenReturn(1);
        Mockito.when(mockCursor.getColumnIndex(DbHelper.TITLE_COLUMN)).thenReturn(2);
        Mockito.when(mockCursor.getColumnIndex(DbHelper.CUR_SUM_COLUMN)).thenReturn(3);
        Mockito.when(mockCursor.getColumnIndex(DbHelper.CURRENCY_COLUMN)).thenReturn(4);

        Mockito.when(mockCursor.getLong(1)).thenReturn(1L);
        Mockito.when(mockCursor.getString(2)).thenReturn("title");
        Mockito.when(mockCursor.getInt(3)).thenReturn(100);
        Mockito.when(mockCursor.getString(4)).thenReturn("NON");

        List<Account> expected = new ArrayList<>();
        expected.add(new Account(1, "title", 100, "NON", 0));

        assertEquals(expected, repo.getListFromCursor(mockCursor));

        assertEquals(new ArrayList<>(), repo.getListFromCursor(null));
    }
}