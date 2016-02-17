package com.blogspot.e_kanivets.moneytracker.controller;

import com.blogspot.e_kanivets.moneytracker.DbHelper;
import com.blogspot.e_kanivets.moneytracker.model.Category;
import com.blogspot.e_kanivets.moneytracker.model.Period;
import com.blogspot.e_kanivets.moneytracker.model.Record;
import com.blogspot.e_kanivets.moneytracker.repo.IRepo;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller class to encapsulate record handling logic.
 * Created on 1/22/16.
 *
 * @author Evgenii Kanivets
 */
public class RecordController extends BaseController<Record> {
    private final CategoryController categoryController;
    private final AccountController accountController;

    public RecordController(IRepo<Record> recordRepo, CategoryController categoryController,
                            AccountController accountController) {
        super(recordRepo);
        this.categoryController = categoryController;
        this.accountController = accountController;
    }

    @Override
    @SuppressWarnings("SimplifiableIfStatement")
    public Record create(Record record) {
        record.setCategoryId(categoryController.readOrCreate(record.getCategory()).getId());

        Record createdRecord = repo.create(record);
        if (createdRecord == null) return null;
        else {
            accountController.recordAdded(createdRecord);
            return createdRecord;
        }
    }

    @Override
    @SuppressWarnings("SimplifiableIfStatement")
    public Record update(Record record) {
        record.setCategoryId(categoryController.readOrCreate(record.getCategory()).getId());

        Record oldRecord = repo.read(record.getId());

        Record updatedRecord = repo.update(record);
        if (updatedRecord == null) return null;
        else {
            accountController.recordUpdated(oldRecord, updatedRecord);
            return updatedRecord;
        }
    }

    @Override
    @SuppressWarnings("SimplifiableIfStatement")
    public boolean delete(Record record) {
        if (repo.delete(record)) return accountController.recordDeleted(record);
        else return false;
    }

    public List<Record> getRecordsForPeriod(Period period) {
        String condition = DbHelper.TIME_COLUMN + " BETWEEN ? AND ?";
        String[] args = new String[]{Long.toString(period.getFirst().getTime()),
                Long.toString(period.getLast().getTime())};

        return repo.readWithCondition(condition, args);
    }

    public List<String> getRecordsForExport(long fromDate, long toDate) {
        final String DELIMITER = ";";
        List<String> result = new ArrayList<>();

        /* First of all add a header */
        @SuppressWarnings("StringBufferReplaceableByString")
        StringBuilder sb = new StringBuilder();
        sb.append(DbHelper.ID_COLUMN).append(DELIMITER);
        sb.append(DbHelper.TIME_COLUMN).append(DELIMITER);
        sb.append(DbHelper.TITLE_COLUMN).append(DELIMITER);
        sb.append(DbHelper.CATEGORY_ID_COLUMN).append(DELIMITER);
        sb.append(DbHelper.PRICE_COLUMN);

        result.add(sb.toString());

        String condition = DbHelper.TIME_COLUMN + " BETWEEN ? AND ?";
        String[] args = new String[]{Long.toString(fromDate), Long.toString(toDate)};

        List<Record> recordList = repo.readWithCondition(condition, args);

        for (Record record : recordList) {
            sb = new StringBuilder();
            sb.append(record.getId()).append(DELIMITER);
            sb.append(record.getTime()).append(DELIMITER);
            sb.append(record.getTitle()).append(DELIMITER);
            Category category = categoryController.read(record.getCategoryId());
            sb.append(category == null ? "NONE" : category.getName()).append(DELIMITER);
            sb.append(record.getType() == 0 ? record.getPrice() : -record.getPrice());

            result.add(sb.toString());
        }

        return result;
    }
}