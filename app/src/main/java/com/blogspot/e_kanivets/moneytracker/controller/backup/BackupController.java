package com.blogspot.e_kanivets.moneytracker.controller.backup;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.blogspot.e_kanivets.moneytracker.controller.FormatController;
import com.blogspot.e_kanivets.moneytracker.controller.backup.tasks.DropboxBackupAsyncTask;
import com.blogspot.e_kanivets.moneytracker.controller.backup.tasks.DropboxFetchBackupListAsyncTask;
import com.blogspot.e_kanivets.moneytracker.controller.backup.tasks.DropboxRestoreBackupAsyncTask;
import com.dropbox.core.v2.DbxClientV2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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

    public void makeBackup(@NonNull DbxClientV2 dbClient,
                           @Nullable OnBackupListener listener) {
        FileInputStream fileInputStream = readAppDb();
        long fileLength = readAppDbFileLength();
        if (fileInputStream == null) return;

        DropboxBackupAsyncTask asyncTask = new DropboxBackupAsyncTask(dbClient,
                formatController.formatDateAndTime(System.currentTimeMillis()),
                fileInputStream, fileLength, listener);
        asyncTask.execute();
    }

    public void restoreBackup(@NonNull DbxClientV2 dbClient, @NonNull String backupName,
                              @Nullable final OnRestoreBackupListener listener) {
        final File file = new File(getRestoreFileName());
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (outputStream == null) {
            if (listener != null) listener.onRestoreFailure(null);
        } else {
            final FileOutputStream finalOutputStream = outputStream;
            DropboxRestoreBackupAsyncTask asyncTask = new DropboxRestoreBackupAsyncTask(dbClient,
                    backupName, outputStream, new OnRestoreBackupListener() {
                @Override
                public void onRestoreSuccess() {
                    try {
                        finalOutputStream.close();
                    } catch (IOException e) {
                        if (listener != null) listener.onRestoreFailure(null);
                        e.printStackTrace();
                    }

                    if (file.exists() && file.length() != 0) {
                        boolean renamed = file.renameTo(new File(getAppDbFileName()));
                        if (listener != null) {
                            if (renamed) listener.onRestoreSuccess();
                            else listener.onRestoreFailure(null);
                        }
                    }
                }

                @Override
                public void onRestoreFailure(String reason) {
                    if (listener != null) listener.onRestoreFailure(reason);
                }
            });
            asyncTask.execute();
        }
    }

    public void fetchBackups(@NonNull DbxClientV2 dbClient,
                             @Nullable OnFetchBackupListListener listener) {
        DropboxFetchBackupListAsyncTask asyncTask = new DropboxFetchBackupListAsyncTask(dbClient, listener);
        asyncTask.execute();
    }

    @Nullable
    private FileInputStream readAppDb() {
        File dbFile = new File(getAppDbFileName());
        FileInputStream fis = null;

        try {
            fis = new FileInputStream(dbFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return fis;
    }

    private long readAppDbFileLength() {
        File dbFile = new File(getAppDbFileName());

        if (dbFile.exists()) return dbFile.length();
        else return 0;
    }

    @NonNull
    private String getAppDbFileName() {
        return filesDir + "/databases/database";
    }

    @NonNull
    private String getRestoreFileName() {
        return getAppDbFileName() + ".restore";
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
