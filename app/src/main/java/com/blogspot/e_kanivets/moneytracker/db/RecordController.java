package com.blogspot.e_kanivets.moneytracker.db;

import com.blogspot.e_kanivets.moneytracker.model.Record;

import java.util.Date;
import java.util.List;

/**
 * Interface for controlling of records. Every record has to have a unique id.
 * Created on 9/1/15.
 *
 * @author Evgenii Kanivets
 */
public interface RecordController {
    /**
     * Adds a given record to the storage. Checks if such record has already exists,
     * if yes the record will not be added.
     *
     * @param record record to add
     * @return true if added, false if not
     */
    boolean addRecord(Record record);

    /**
     * Deletes a record from storage if such record exists.
     *
     * @param record record to remove
     * @return true if record was deleted, false if not
     */
    boolean deleteRecord(Record record);

    /**
     * Updates a record in storage if exists, if no - nothing will happen.
     *
     * @param record record to update
     * @return true if record was changed, false if not
     */
    boolean updateRecord(Record record);

    /**
     * Fetches records from {@code fromTime} to {@code toTime} from storage.
     *
     * @param fromTime {@link Date} representation of period start
     * @param toTime   {@link Date} representation of period end
     * @return list of mathced records
     */
    List<Record> getRecords(Date fromTime, Date toTime);
}