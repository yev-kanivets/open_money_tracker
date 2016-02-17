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
public class RecordController {
    private final IRepo<Category> categoryRepo;
    private final IRepo<Record> recordRepo;
    private final CategoryController categoryController;
    private final AccountController accountController;

    public RecordController(IRepo<Record> recordRepo, IRepo<Category> categoryRepo,
                            CategoryController categoryController, AccountController accountController) {
        this.recordRepo = recordRepo;
        this.categoryRepo = categoryRepo;
        this.categoryController = categoryController;
        this.accountController = accountController;
    }

    @SuppressWarnings("SimplifiableIfStatement")
    public boolean create(Record record) {
        record.setCategoryId(categoryController.readOrCreate(record.getCategory()).getId());

        Record createdRecord = recordRepo.create(record);
        if (createdRecord == null) return false;
        else return accountController.recordAdded(createdRecord);
    }

    @SuppressWarnings("SimplifiableIfStatement")
    public boolean update(Record record) {
        record.setCategoryId(categoryController.readOrCreate(record.getCategory()).getId());

        Record oldRecord = recordRepo.read(record.getId());

        Record updatedRecord = recordRepo.update(record);
        if (updatedRecord == null) return false;
        else return accountController.recordUpdated(oldRecord, updatedRecord);
    }

    @SuppressWarnings("SimplifiableIfStatement")
    public boolean delete(Record record) {
        if (recordRepo.delete(record)) return accountController.recordDeleted(record);
        else return false;
    }

    public List<Record> getRecordsForPeriod(Period period) {
        String condition = DbHelper.TIME_COLUMN  + " BETWEEN ? AND ?";
        String[] args = new String[]{Long.toString(period.getFirst().getTime()),
                Long.toString(period.getLast().getTime())};

        return recordRepo.readWithCondition(condition, args);
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

        String condition = DbHelper.TIME_COLUMN  + " BETWEEN ? AND ?";
        String[] args = new String[]{Long.toString(fromDate), Long.toString(toDate)};

        List<Record> recordList = recordRepo.readWithCondition(condition, args);

        for (Record record : recordList) {
            sb = new StringBuilder();
            sb.append(record.getId()).append(DELIMITER);
            sb.append(record.getTime()).append(DELIMITER);
            sb.append(record.getTitle()).append(DELIMITER);
            Category category = categoryRepo.read(record.getCategoryId());
            sb.append(category == null ? "NONE" : category.getName()).append(DELIMITER);
            sb.append(record.getType() == 0 ? record.getPrice() : -record.getPrice());

            result.add(sb.toString());
        }

        return result;
    }
}