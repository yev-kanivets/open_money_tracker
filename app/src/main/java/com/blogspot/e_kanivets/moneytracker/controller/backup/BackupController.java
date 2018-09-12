package com.blogspot.e_kanivets.moneytracker.controller.backup;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.blogspot.e_kanivets.moneytracker.controller.FormatController;
import com.blogspot.e_kanivets.moneytracker.controller.backup.tasks.DropboxBackupAsyncTask;
import com.blogspot.e_kanivets.moneytracker.controller.backup.tasks.DropboxFetchBackupListAsyncTask;
import com.blogspot.e_kanivets.moneytracker.controller.backup.tasks.DropboxRestoreBackupAsyncTask;
import com.dropbox.core.v2.DbxClientV2;

import java.util.List;

/**
 * Controller class to encapsulate backup logic.
 * Created on 8/10/16.
 *
 * @author Evgenii Kanivets
 */
public class BackupController {

    private FormatController formatController;
    private String filesDir;

    public BackupController(FormatController formatController, String filesDir) {
        this.formatController = formatController;
        this.filesDir = filesDir;
    }

    public void makeBackup(@NonNull DbxClientV2 dbClient, @Nullable OnBackupListener listener) {
        DropboxBackupAsyncTask asyncTask =
                new DropboxBackupAsyncTask(dbClient, formatController.formatDateAndTime(System.currentTimeMillis()),
                        getAppDbFileName(), listener);
        asyncTask.execute();
    }

    public void restoreBackup(@NonNull DbxClientV2 dbClient, @NonNull String backupName,
            @Nullable final OnRestoreBackupListener listener) {
        DropboxRestoreBackupAsyncTask asyncTask =
                new DropboxRestoreBackupAsyncTask(dbClient, getAppDbFileName(), backupName, listener);
        asyncTask.execute();
    }

    public void fetchBackups(@NonNull DbxClientV2 dbClient, @Nullable OnFetchBackupListListener listener) {
        DropboxFetchBackupListAsyncTask asyncTask = new DropboxFetchBackupListAsyncTask(dbClient, listener);
        asyncTask.execute();
    }

    @NonNull private String getAppDbFileName() {
        return filesDir + "/databases/database";
    }

    public interface OnBackupListener {
        String SUCCESS = "success";
        String ERROR_AUTHENTICATION = "error_authentication";

        void onBackupSuccess();

        void onBackupFailure(String reason);
    }

    public interface OnFetchBackupListListener {
        void onBackupsFetched(@NonNull List<String> backupList);
    }

    public interface OnRestoreBackupListener {
        String ERROR_AUTHENTICATION = "error_authentication";

        void onRestoreSuccess();

        void onRestoreFailure(String reason);
    }
}
