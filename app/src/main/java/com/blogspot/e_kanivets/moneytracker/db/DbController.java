package com.blogspot.e_kanivets.moneytracker.db;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Interface of the interaction with a Database. Insert, update, delete, query operation's
 * implementations.
 * Created on 9/1/15.
 *
 * @author Evgenii Kanivets
 */
public interface DbController {
    /**
     * Inserts a specified record into {@code tableName} with {@code id}.
     *
     * @param tableName     table name of table to insert a record
     * @param id            id of the record
     * @param contentValues map representation of the record
     * @return true if inserted
     */
    boolean insert(String tableName, String id, ContentValues contentValues);

    /**
     * @param tableName     table name of table to insert a record
     * @param id            id of the record
     * @param contentValues map representation of the record
     * @return true if updated
     */
    boolean update(String tableName, String id, ContentValues contentValues);

    /**
     * @param tableName table name of table to insert a record
     * @param id        id of the record
     * @return true if deleted
     */
    boolean delete(String tableName, String id);

    /**
     * @param tableName table name of table to insert a record
     * @param selection selection statement
     * @return Cursor with a matched records
     */
    Cursor query(String tableName, String selection);
}