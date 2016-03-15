package com.blogspot.e_kanivets.moneytracker.activity;

import android.os.Environment;

import com.blogspot.e_kanivets.moneytracker.DbHelper;
import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.activity.base.BaseBackActivity;
import com.blogspot.e_kanivets.moneytracker.controller.AccountController;
import com.blogspot.e_kanivets.moneytracker.controller.CategoryController;
import com.blogspot.e_kanivets.moneytracker.controller.RecordController;
import com.blogspot.e_kanivets.moneytracker.entity.Category;
import com.blogspot.e_kanivets.moneytracker.entity.Record;
import com.blogspot.e_kanivets.moneytracker.repo.AccountRepo;
import com.blogspot.e_kanivets.moneytracker.repo.CategoryRepo;
import com.blogspot.e_kanivets.moneytracker.repo.RecordRepo;
import com.blogspot.e_kanivets.moneytracker.repo.base.IRepo;
import com.blogspot.e_kanivets.moneytracker.util.Constants;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

import butterknife.OnClick;

public class ExportActivity extends BaseBackActivity {
    private static final String TAG = "ExportActivity";

    @Override
    protected int getContentViewId() {
        return R.layout.activity_export;
    }

    @OnClick(R.id.btn_export)
    public void exportRecords() {
        DbHelper dbHelper = new DbHelper(ExportActivity.this);
        IRepo<Category> categoryRepo = new CategoryRepo(dbHelper);
        CategoryController categoryController = new CategoryController(categoryRepo);
        AccountController accountController = new AccountController(new AccountRepo(dbHelper));
        IRepo<Record> recordRepo = new RecordRepo(dbHelper);

        RecordController recordController = new RecordController(recordRepo, categoryController, accountController);

        List<String> records = recordController.getRecordsForExport(0, Long.MAX_VALUE);

        File outFile = new File(Environment.getExternalStorageDirectory(), Constants.DEFAULT_EXPORT_FILE_NAME);
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(outFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (pw != null) {
            for (String record : records) {
                pw.println(record);
                pw.flush();
            }

            pw.flush();
            pw.close();
        }
    }
}
