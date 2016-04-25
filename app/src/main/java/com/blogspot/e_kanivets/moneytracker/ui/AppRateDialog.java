package com.blogspot.e_kanivets.moneytracker.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.blogspot.e_kanivets.moneytracker.MtApp;
import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.controller.PreferenceController;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class AppRateDialog extends AlertDialog {
    private static final String GP_MARKET = "market://details?id=";

    private Context context;

    @Inject
    PreferenceController preferenceController;

    public AppRateDialog(Context context) {
        super(context);
        this.context = context;
        MtApp.get().getAppComponent().inject(AppRateDialog.this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_rate);
        ButterKnife.bind(AppRateDialog.this);
    }

    @OnClick(R.id.yes_button)
    public void yes() {
        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(GP_MARKET + context.getPackageName())));
        preferenceController.appRated();
        dismiss();
    }

    @OnClick(R.id.maybeButton)
    public void maybe() {
        dismiss();
    }

    @OnClick(R.id.thanksButton)
    public void thanks() {
        preferenceController.appRated();
        dismiss();
    }

    @Override
    public void dismiss() {
        try {
            super.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}