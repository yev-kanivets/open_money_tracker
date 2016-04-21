package com.blogspot.e_kanivets.moneytracker.controller.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.blogspot.e_kanivets.moneytracker.repo.DbHelper;
import com.blogspot.e_kanivets.moneytracker.controller.base.BaseController;
import com.blogspot.e_kanivets.moneytracker.entity.data.Account;
import com.blogspot.e_kanivets.moneytracker.entity.data.Category;
import com.blogspot.e_kanivets.moneytracker.entity.Period;
import com.blogspot.e_kanivets.moneytracker.entity.data.Record;
import com.blogspot.e_kanivets.moneytracker.repo.base.IRepo;

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
    public Record create(@Nullable Record record) {
        if (record == null) return null;

        record = validateRecord(record);

        Record createdRecord = repo.create(record);
        if (createdRecord == null) return null;
        else {
            accountController.recordAdded(createdRecord);
            return createdRecord;
        }
    }

    @Override
    @SuppressWarnings("SimplifiableIfStatement")
    public Record update(@Nullable Record record) {
        if (record == null) return null;

        record = validateRecord(record);

        Record oldRecord = read(record.getId());

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

    @Nullable
    @Override
    public Record read(long id) {
        List<Record> list = readWithCondition("id=?", new String[]{Long.toString(id)});

        if (list.size() == 1) return list.get(0);
        else return null;
    }

    @NonNull
    @Override
    public List<Record> readAll() {
        return readWithCondition(null, null);
    }

    @NonNull
    @Override
    public List<Record> readWithCondition(String condition, String[] args) {
        List<Record> recordList = super.readWithCondition(condition, args);

        // Data read from DB through Repo layer doesn't contain right nested objects, so construct them
        List<Record> completedRecordList = new ArrayList<>();
        for (Record record : recordList) {
            Category category = null;
            if (record.getCategory() != null)
                category = categoryController.read(record.getCategory().getId());

            Account account = null;
            if (record.getAccount() != null)
                account = accountController.read(record.getAccount().getId());

            completedRecordList.add(new Record(record.getId(), record.getTime(), record.getType(),
                    record.getTitle(), category, record.getPrice(), account, record.getCurrency()));
        }

        return completedRecordList;
    }

    public List<Record> getRecordsForPeriod(Period period) {
        String condition = DbHelper.TIME_COLUMN + " BETWEEN ? AND ?";
        String[] args = new String[]{Long.toString(period.getFirst().getTime()),
                Long.toString(period.getLast().getTime())};

        return readWithCondition(condition, args);
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

        List<Record> recordList = readWithCondition(condition, args);

        for (Record record : recordList) {
            sb = new StringBuilder();
            sb.append(record.getId()).append(DELIMITER);
            sb.append(record.getTime()).append(DELIMITER);
            sb.append(record.getTitle()).append(DELIMITER);

            Category category = null;
            if (record.getCategory() != null)
                category = categoryController.read(record.getCategory().getId());

            sb.append(category == null ? "NONE" : category.getName()).append(DELIMITER);
            sb.append(record.getType() == 0 ? record.getPrice() : -record.getPrice());

            result.add(sb.toString());
        }

        return result;
    }

    private Record validateRecord(@NonNull Record record) {
        if (record.getCategory() == null) return record;

        Category category = categoryController.readOrCreate(record.getCategory().getName());

        return new Record(record.getId(), record.getTime(), record.getType(), record.getTitle(),
                category, record.getPrice(), record.getAccount(), record.getCurrency());
    }
}