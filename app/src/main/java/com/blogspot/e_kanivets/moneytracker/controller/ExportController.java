package com.blogspot.e_kanivets.moneytracker.controller;

import com.blogspot.e_kanivets.moneytracker.controller.data.CategoryController;
import com.blogspot.e_kanivets.moneytracker.controller.data.RecordController;
import com.blogspot.e_kanivets.moneytracker.entity.data.Category;
import com.blogspot.e_kanivets.moneytracker.entity.data.Record;
import com.blogspot.e_kanivets.moneytracker.repo.DbHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller class to encapsulate export logic.
 * Created on 6/28/16.
 *
 * @author Evgenii Kanivets
 */
public class ExportController {
    private static final String HEAD_TIME = "time";
    private static final String HEAD_TITLE = "title";
    private static final String HEAD_CATEGORY = "category";
    private static final String HEAD_PRICE = "price";
    private static final String HEAD_CURRENCY = "currency";
    private static final String DELIMITER = ";";

    private final RecordController recordController;
    private final CategoryController categoryController;

    public ExportController(RecordController recordController, CategoryController categoryController) {
        this.recordController = recordController;
        this.categoryController = categoryController;
    }

    public List<String> getRecordsForExport(long fromDate, long toDate) {
        List<String> result = new ArrayList<>();

        /* First of all add a header */
        @SuppressWarnings("StringBufferReplaceableByString")
        StringBuilder sb = new StringBuilder();
        sb.append(HEAD_TIME).append(DELIMITER);
        sb.append(HEAD_TITLE).append(DELIMITER);
        sb.append(HEAD_CATEGORY).append(DELIMITER);
        sb.append(HEAD_PRICE).append(DELIMITER);
        sb.append(HEAD_CURRENCY);

        result.add(sb.toString());

        String condition = DbHelper.TIME_COLUMN + " BETWEEN ? AND ?";
        String[] args = new String[]{Long.toString(fromDate), Long.toString(toDate)};

        List<Record> recordList = recordController.readWithCondition(condition, args);

        for (Record record : recordList) {
            sb = new StringBuilder();
            sb.append(record.getId()).append(DELIMITER);
            sb.append(record.getTime()).append(DELIMITER);
            sb.append(record.getTitle()).append(DELIMITER);

            Category category = null;
            if (record.getCategory() != null)
                category = categoryController.read(record.getCategory().getId());

            sb.append(category == null ? "NONE" : category.getName()).append(DELIMITER);
            sb.append(record.getType() == 0 ? record.getFullPrice()
                    : -record.getFullPrice()).append(DELIMITER);
            sb.append(record.getCurrency() == null ? DbHelper.DEFAULT_ACCOUNT_CURRENCY
                    : record.getCurrency());

            result.add(sb.toString());
        }

        return result;
    }
}
