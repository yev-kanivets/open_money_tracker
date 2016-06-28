package com.blogspot.e_kanivets.moneytracker.activity.external;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.activity.base.BaseBackActivity;
import com.blogspot.e_kanivets.moneytracker.controller.external.ExportController;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

import javax.inject.Inject;

import butterknife.OnClick;
import timber.log.Timber;

public class ExportActivity extends BaseBackActivity {
    @SuppressWarnings("unused")
    private static final String TAG = "ExportActivity";

    private static final String DEFAULT_EXPORT_FILE_NAME = "money_tracker.csv";

    @Inject
    ExportController exportController;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_export;
    }

    @Override
    protected boolean initData() {
        boolean result = super.initData();
        getAppComponent().inject(ExportActivity.this);
        return result;
    }

    @OnClick(R.id.btn_export)
    public void exportRecords() {
        List<String> records = exportController.getRecordsForExport(0, Long.MAX_VALUE);

        File exportDir = new File(getCacheDir(), "export");
        boolean exportDirCreated = exportDir.mkdirs();
        Timber.d("ExportDirCreated: %b", exportDirCreated);

        File outFile;
        if (exportDir.exists()) outFile = new File(exportDir, DEFAULT_EXPORT_FILE_NAME);
        else return;

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

            shareExportedRecords(outFile);
        }
    }

    private void shareExportedRecords(@NonNull File exportFile) {
        Uri fileUri = FileProvider.getUriForFile(ExportActivity.this, getPackageName(), exportFile);

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_STREAM, fileUri);
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, "Share exported records"));
    }
}
