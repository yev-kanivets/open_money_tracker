package com.blogspot.e_kanivets.moneytracker.controller.backup.tasks;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.blogspot.e_kanivets.moneytracker.controller.backup.BackupController;
import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import java.io.FileInputStream;
import java.io.IOException;

public class DropboxBackupAsyncTask extends AsyncTask<Void, String, String> {
    private DbxClientV2 dbClient;
    private String fileName;
    private FileInputStream fileInputStream;
    private long fileLength;

    @Nullable
    private BackupController.OnBackupListener listener;

    public DropboxBackupAsyncTask(@NonNull DbxClientV2 dbClient, String fileName,
            FileInputStream fileInputStream, long fileLength,
            @Nullable BackupController.OnBackupListener listener) {
        this.dbClient = dbClient;
        this.fileName = fileName;
        this.fileInputStream = fileInputStream;
        this.fileLength = fileLength;
        this.listener = listener;
    }

    @Override
    protected String doInBackground(Void... params) {
        FileMetadata info = null;

        try {
            info = dbClient.files().upload("/" + fileName).uploadAndFinish(fileInputStream);
        } catch (DbxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (info == null) return null;
        else return BackupController.OnBackupListener.SUCCESS;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (listener == null) return;

        if (BackupController.OnBackupListener.SUCCESS.equals(result)) listener.onBackupSuccess();
        else listener.onBackupFailure(result);
    }
}
