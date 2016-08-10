package com.blogspot.e_kanivets.moneytracker.controller;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.blogspot.e_kanivets.moneytracker.BuildConfig;
import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.exception.DropboxUnlinkedException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Controller class to encapsulate backup logic.
 * Created on 8/10/16.
 *
 * @author Evgenii Kanivets
 */
public class BackupController {
    private static final String PACKAGE_NAME = BuildConfig.APPLICATION_ID;

    private FormatController formatController;
    private String filesDir;

    public BackupController(FormatController formatController, String filesDir) {
        this.formatController = formatController;
        this.filesDir = filesDir;
    }

    public void makeBackup(@NonNull DropboxAPI<AndroidAuthSession> dbApi,
                           @Nullable OnBackupListener listener) {
        FileInputStream fileInputStream = readAppDb();
        long fileLength = readAppDbFileLength();
        if (fileInputStream == null) return;

        DropboxBackupAsyncTask asyncTask = new DropboxBackupAsyncTask(dbApi,
                formatController.formatDateAndTime(System.currentTimeMillis()),
                fileInputStream, fileLength, listener);
        asyncTask.execute();
    }

    public void fetchBackups(@NonNull DropboxAPI<AndroidAuthSession> dbApi,
                             @Nullable OnFetchBackupListListener listener) {
        DropboxFetchBackupListAsyncTask asyncTask = new DropboxFetchBackupListAsyncTask(dbApi, listener);
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
        return filesDir + PACKAGE_NAME + "/databases/database";
    }

    private class DropboxBackupAsyncTask extends AsyncTask<Void, String, String> {
        private DropboxAPI<AndroidAuthSession> dbApi;
        private String fileName;
        private FileInputStream fileInputStream;
        private long fileLength;

        @Nullable
        private OnBackupListener listener;

        public DropboxBackupAsyncTask(DropboxAPI<AndroidAuthSession> dbApi, String fileName,
                                      FileInputStream fileInputStream, long fileLength,
                                      @Nullable OnBackupListener listener) {
            this.dbApi = dbApi;
            this.fileName = fileName;
            this.fileInputStream = fileInputStream;
            this.fileLength = fileLength;
            this.listener = listener;
        }

        @Override
        protected String doInBackground(Void... params) {
            DropboxAPI.Entry response = null;
            try {
                response = dbApi.putFile(fileName, fileInputStream, fileLength, null, null);
            } catch (DropboxUnlinkedException e) {
                e.printStackTrace();
                return OnBackupListener.ERROR_AUTHENTICATION;
            } catch (DropboxException e) {
                e.printStackTrace();
            }

            if (response == null) return null;
            else return OnBackupListener.SUCCESS;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (listener == null) return;

            if (OnBackupListener.SUCCESS.equals(result)) listener.onBackupSuccess();
            else listener.onBackupFailure(result);
        }
    }

    private class DropboxFetchBackupListAsyncTask extends AsyncTask<Void, List<String>, List<String>> {
        private DropboxAPI<AndroidAuthSession> dbApi;

        @Nullable
        private OnFetchBackupListListener listener;

        public DropboxFetchBackupListAsyncTask(DropboxAPI<AndroidAuthSession> dbApi,
                                               @Nullable OnFetchBackupListListener listener) {
            this.dbApi = dbApi;
            this.listener = listener;
        }

        @Override
        protected List<String> doInBackground(Void... params) {
            List<DropboxAPI.Entry> entryList = new ArrayList<>();
            List<String> backupList = new ArrayList<>();

            try {
                DropboxAPI.Entry entry = dbApi.metadata("/", -1, null, true, null);
                entryList = entry.contents;
            } catch (DropboxException e) {
                e.printStackTrace();
            }

            for (DropboxAPI.Entry entry : entryList) {
                backupList.add(entry.fileName());
            }

            return backupList;
        }

        @Override
        protected void onPostExecute(List<String> backupList) {
            super.onPostExecute(backupList);
            if (listener == null) return;

            Collections.reverse(backupList);
            listener.onBackupsFetched(backupList);
        }
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
}
