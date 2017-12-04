package com.blogspot.e_kanivets.moneytracker.controller;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.Metadata;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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

    private static class DropboxBackupAsyncTask extends AsyncTask<Void, String, String> {
        private DbxClientV2 dbClient;
        private String fileName;
        private FileInputStream fileInputStream;
        private long fileLength;

        @Nullable
        private OnBackupListener listener;

        public DropboxBackupAsyncTask(@NonNull DbxClientV2 dbClient, String fileName,
                                      FileInputStream fileInputStream, long fileLength,
                                      @Nullable OnBackupListener listener) {
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

    private static class DropboxFetchBackupListAsyncTask extends AsyncTask<Void, List<String>, List<String>> {
        private DbxClientV2 dbClient;

        @Nullable
        private OnFetchBackupListListener listener;

        public DropboxFetchBackupListAsyncTask(DbxClientV2 dbClient,
                                               @Nullable OnFetchBackupListListener listener) {
            this.dbClient = dbClient;
            this.listener = listener;
        }

        @Override
        protected List<String> doInBackground(Void... params) {
            List<Metadata> entryList = new ArrayList<>();
            List<String> backupList = new ArrayList<>();

            try {
                entryList = dbClient.files().listFolder("").getEntries();
            } catch (DbxException e) {
                e.printStackTrace();
            }

            for (Metadata entry : entryList) {
                backupList.add(entry.getName());
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

    private static class DropboxRestoreBackupAsyncTask extends AsyncTask<Void, String, String> {
        private DbxClientV2 dbClient;
        private String backupName;
        private FileOutputStream outputStream;

        @Nullable
        private OnRestoreBackupListener listener;

        public DropboxRestoreBackupAsyncTask(DbxClientV2 dbClient, String backupName,
                                             FileOutputStream outputStream,
                                             @Nullable OnRestoreBackupListener listener) {
            this.dbClient = dbClient;
            this.backupName = backupName;
            this.outputStream = outputStream;
            this.listener = listener;
        }

        @Override
        protected String doInBackground(Void... params) {
            FileMetadata info = null;
            try {
                info = dbClient.files().download("/" + backupName).download(outputStream);
            } catch (DbxException | IOException e) {
                e.printStackTrace();
            }

            if (info == null) return null;
            else return OnBackupListener.SUCCESS;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (listener == null) return;

            if (OnBackupListener.SUCCESS.equals(result)) listener.onRestoreSuccess();
            else listener.onRestoreFailure(result);
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

    public interface OnRestoreBackupListener {
        String ERROR_AUTHENTICATION = "error_authentication";

        void onRestoreSuccess();

        void onRestoreFailure(String reason);
    }
}
