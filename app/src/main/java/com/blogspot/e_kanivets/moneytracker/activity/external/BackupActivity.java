package com.blogspot.e_kanivets.moneytracker.activity.external;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.activity.base.BaseBackActivity;
import com.blogspot.e_kanivets.moneytracker.controller.BackupController;
import com.blogspot.e_kanivets.moneytracker.controller.PreferenceController;
import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AppKeyPair;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
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
        startProgress();
        backupController.makeBackup(dbApi, new BackupController.OnBackupListener() {
            @Override
            public void onBackupSuccess() {
                Timber.d("Backup success.");
                stopProgress();
                fetchBackups();
            }

            @Override
            public void onBackupFailure(String reason) {
                Timber.d("Backup failure.");
                stopProgress();
                showToast(R.string.failed_create_backup);

                if (BackupController.OnBackupListener.ERROR_AUTHENTICATION.equals(reason)) {
                    preferenceController.writeDropboxAccessToken(null);
                    dbApi.getSession().startOAuth2Authentication(BackupActivity.this);
                    btnBackupNow.setEnabled(false);
                }
            }
        });
    }

    private void fetchBackups() {
        startProgress();
        backupController.fetchBackups(dbApi, new BackupController.OnFetchBackupListListener() {
            @Override
            public void onBackupsFetched(@NonNull List<String> backupList) {
                stopProgress();
                ArrayAdapter<String> adapter = new ArrayAdapter<>(BackupActivity.this,
                        android.R.layout.simple_list_item_1, backupList);
                listView.setAdapter(adapter);
            }
        });
    }
}
