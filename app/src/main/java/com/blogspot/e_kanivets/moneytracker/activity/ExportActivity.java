package com.blogspot.e_kanivets.moneytracker.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;

import com.blogspot.e_kanivets.moneytracker.MtApp;
import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.activity.base.BaseBackActivity;
import com.blogspot.e_kanivets.moneytracker.controller.RecordController;
import com.blogspot.e_kanivets.moneytracker.util.Constants;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

import javax.inject.Inject;

import butterknife.OnClick;

public class ExportActivity extends BaseBackActivity {
    @SuppressWarnings("unused")
    private static final String TAG = "ExportActivity";

    @Inject
    RecordController recordController;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_export;
    }

    @Override
    protected boolean initData() {
        boolean result = super.initData();
        MtApp.get().getAppComponent().inject(ExportActivity.this);
        return result;
    }

    @OnClick(R.id.btn_export)
    public void exportRecords() {
        List<String> records = recordController.getRecordsForExport(0, Long.MAX_VALUE);

        File exportDir = new File(getCacheDir(), "export");
        boolean exportDirCreated = exportDir.mkdirs();

        File outFile;
        if (exportDirCreated) outFile = new File(exportDir, Constants.DEFAULT_EXPORT_FILE_NAME);
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
