package com.blogspot.e_kanivets.moneytracker.controller.backup.tasks;

import android.os.AsyncTask;
import android.support.annotation.Nullable;
import com.blogspot.e_kanivets.moneytracker.controller.backup.BackupController;
import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import java.io.FileOutputStream;
import java.io.IOException;

public class DropboxRestoreBackupAsyncTask extends AsyncTask<Void, String, String> {
    private DbxClientV2 dbClient;
    private String backupName;
    private FileOutputStream outputStream;

    @Nullable private BackupController.OnRestoreBackupListener listener;

    public DropboxRestoreBackupAsyncTask(DbxClientV2 dbClient, String backupName, FileOutputStream outputStream,
            @Nullable BackupController.OnRestoreBackupListener listener) {
        this.dbClient = dbClient;
        this.backupName = backupName;
        this.outputStream = outputStream;
        this.listener = listener;
    }

    @Override protected String doInBackground(Void... params) {
        FileMetadata info = null;
        try {
            info = dbClient.files().download("/" + backupName).download(outputStream);
        } catch (DbxException | IOException e) {
            e.printStackTrace();
        }

        if (info == null) {
            return null;
        } else {
            return BackupController.OnBackupListener.SUCCESS;
        }
    }

    @Override protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (listener == null) return;

        if (BackupController.OnBackupListener.SUCCESS.equals(result)) {
            listener.onRestoreSuccess();
        } else {
            listener.onRestoreFailure(result);
        }
    }
}
