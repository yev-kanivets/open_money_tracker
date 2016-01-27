package com.blogspot.e_kanivets.moneytracker.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.util.PrefUtils;
import com.blogspot.e_kanivets.moneytracker.util.Constants;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AppRateDialog extends AlertDialog {
    private Context context;

    public AppRateDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_rate);
        ButterKnife.bind(AppRateDialog.this);
    }

    @OnClick(R.id.yes_button)
    public void yes() {
        context.startActivity(new Intent(Intent.ACTION_VIEW,
                Uri.parse(Constants.GP_MARKET + context.getPackageName())));
        PrefUtils.appRated();
        dismiss();
    }

    @OnClick(R.id.maybeButton)
    public void maybe() {
        dismiss();
    }

    @OnClick(R.id.thanksButton)
    public void thanks() {
        PrefUtils.appRated();
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