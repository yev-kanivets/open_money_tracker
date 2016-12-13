package com.blogspot.e_kanivets.moneytracker.activity.external;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.blogspot.e_kanivets.moneytracker.MtApp;
import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.activity.base.BaseBackActivity;
import com.blogspot.e_kanivets.moneytracker.controller.BackupController;
import com.blogspot.e_kanivets.moneytracker.controller.PreferenceController;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AppKeyPair;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnItemClick;
import timber.log.Timber;

public class BackupActivity extends BaseBackActivity {
    private static final String APP_KEY = "5lqugcckdy9y6lj";
    private static final String APP_SECRET = "psbu50k9713u68j";

    @Inject
    PreferenceController preferenceController;
    @Inject
    BackupController backupController;

    private DropboxAPI<AndroidAuthSession> dbApi;

    @Bind(R.id.btn_backup_now)
    View btnBackupNow;
    @Bind(R.id.list_view)
    ListView listView;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_backup;
    }

    @Override
    protected boolean initData() {
        getAppComponent().inject(BackupActivity.this);

        AppKeyPair appKeys = new AppKeyPair(APP_KEY, APP_SECRET);
        String accessToken = preferenceController.readDropboxAccessToken();

        AndroidAuthSession session = new AndroidAuthSession(appKeys);
        dbApi = new DropboxAPI<>(session);
        if (accessToken == null) dbApi.getSession().startOAuth2Authentication(BackupActivity.this);
        else {
            dbApi.getSession().setOAuth2AccessToken(accessToken);
            fetchBackups();
        }

        return super.initData();
    }

    @Override
    protected void initViews() {
        super.initViews();
        btnBackupNow.setEnabled(preferenceController.readDropboxAccessToken() != null);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (dbApi.getSession().authenticationSuccessful()) {
            try {
                // Required to complete auth, sets the access token on the session
                dbApi.getSession().finishAuthentication();
                preferenceController.writeDropboxAccessToken(dbApi.getSession().getOAuth2AccessToken());
                btnBackupNow.setEnabled(true);
                fetchBackups();
            } catch (IllegalStateException e) {
                Timber.e("Error authenticating: %s", e.getMessage());
            }
        }
    }

    @OnClick(R.id.btn_backup_now)
    public void backupNow() {
        // Answers event
        Answers.getInstance().logContentView(new ContentViewEvent()
                .putContentName("Make Backup")
                .putContentType("Button"));

        startProgress(getString(R.string.making_backup));
        backupController.makeBackup(dbApi, new BackupController.OnBackupListener() {
            @Override
            public void onBackupSuccess() {
                // Answers event
                Answers.getInstance().logContentView(new ContentViewEvent()
                        .putContentName("Backup success")
                        .putContentType("Event"));

                Timber.d("Backup success.");
                if (isFinishing()) return;

                stopProgress();
                fetchBackups();
            }

            @Override
            public void onBackupFailure(String reason) {
                // Answers event
                Answers.getInstance().logContentView(new ContentViewEvent()
                        .putContentName("Backup failure")
                        .putContentType("Event"));

                Timber.d("Backup failure.");
                if (isFinishing()) return;

                stopProgress();
                showToast(R.string.failed_create_backup);

                if (BackupController.OnBackupListener.ERROR_AUTHENTICATION.equals(reason)) logout();
            }
        });
    }

    @OnItemClick(R.id.list_view)
    public void restoreBackupClicked(int position) {
        // Answers event
        Answers.getInstance().logContentView(new ContentViewEvent()
                .putContentName("Restore backup")
                .putContentType("Button"));

        final String backupName = listView.getAdapter().getItem(position).toString();

        AlertDialog.Builder builder = new AlertDialog.Builder(BackupActivity.this);
        builder.setTitle(getString(R.string.warning));
        builder.setMessage(getString(R.string.want_erase_and_restore, backupName));
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                restoreBackup(backupName);
            }
        });
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.show();
    }

    private void restoreBackup(final String backupName) {
        startProgress(getString(R.string.restoring_backup));
        backupController.restoreBackup(dbApi, backupName, new BackupController.OnRestoreBackupListener() {
            @Override
            public void onRestoreSuccess() {
                // Answers event
                Answers.getInstance().logContentView(new ContentViewEvent()
                        .putContentName("Restore Success")
                        .putContentType("Event"));

                Timber.d("Restore success.");
                if (isFinishing()) return;

                stopProgress();

                AlertDialog.Builder builder = new AlertDialog.Builder(BackupActivity.this);
                builder.setTitle(getString(R.string.backup_is_restored));
                builder.setMessage(getString(R.string.backup_restored, backupName));
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        MtApp.get().buildAppComponent();
                        setResult(RESULT_OK);
                        finish();
                    }
                });
                builder.setPositiveButton(android.R.string.ok, null);
                builder.show();
            }

            @Override
            public void onRestoreFailure(String reason) {
                // Answers event
                Answers.getInstance().logContentView(new ContentViewEvent()
                        .putContentName("Restore Failure")
                        .putContentType("Event"));

                Timber.d("Restore failure.");
                if (isFinishing()) return;

                stopProgress();
                showToast(R.string.failed_restore_backup);

                if (BackupController.OnRestoreBackupListener.ERROR_AUTHENTICATION.equals(reason))
                    logout();
            }
        });
    }

    private void fetchBackups() {
        startProgress(getString(R.string.fetching_backups));
        backupController.fetchBackups(dbApi, new BackupController.OnFetchBackupListListener() {
            @Override
            public void onBackupsFetched(@NonNull List<String> backupList) {
                if (isFinishing()) return;

                stopProgress();
                ArrayAdapter<String> adapter = new ArrayAdapter<>(BackupActivity.this,
                        android.R.layout.simple_list_item_1, backupList);
                listView.setAdapter(adapter);
            }
        });
    }

    private void logout() {
        preferenceController.writeDropboxAccessToken(null);
        dbApi.getSession().startOAuth2Authentication(BackupActivity.this);
        btnBackupNow.setEnabled(false);
    }
}
