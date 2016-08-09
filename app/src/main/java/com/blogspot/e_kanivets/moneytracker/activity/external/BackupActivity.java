package com.blogspot.e_kanivets.moneytracker.activity.external;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.activity.base.BaseBackActivity;
import com.blogspot.e_kanivets.moneytracker.controller.PreferenceController;
import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AppKeyPair;

import javax.inject.Inject;

import timber.log.Timber;

public class BackupActivity extends BaseBackActivity {
    private static final String APP_KEY = "5lqugcckdy9y6lj";
    private static final String APP_SECRET = "psbu50k9713u68j";

    @Inject
    PreferenceController preferenceController;

    private DropboxAPI<AndroidAuthSession> mDBApi;
    private String accessToken;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_backup;
    }

    @Override
    protected boolean initData() {
        getAppComponent().inject(BackupActivity.this);

        AppKeyPair appKeys = new AppKeyPair(APP_KEY, APP_SECRET);
        AndroidAuthSession session = new AndroidAuthSession(appKeys);
        mDBApi = new DropboxAPI<>(session);

        accessToken = preferenceController.readDropboxAccessToken();
        if (accessToken == null) mDBApi.getSession().startOAuth2Authentication(BackupActivity.this);

        return super.initData();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (accessToken == null && mDBApi.getSession().authenticationSuccessful()) {
            try {
                // Required to complete auth, sets the access token on the session
                mDBApi.getSession().finishAuthentication();
                accessToken = mDBApi.getSession().getOAuth2AccessToken();
                preferenceController.writeDropboxAccessToken(accessToken);
            } catch (IllegalStateException e) {
                Timber.e("Error authenticating: %s", e.getMessage());
            }
        }
    }
}
