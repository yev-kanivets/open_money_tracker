package com.blogspot.e_kanivets.moneytracker.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.util.AppUtils;
import com.blogspot.e_kanivets.moneytracker.util.Constants;

public class AppRateDialog extends AlertDialog {

    private Context context;

    public AppRateDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = getLayoutInflater().inflate(R.layout.dialog_rate, null);
        setContentView(view);

        Button yesButton = (Button) view.findViewById(R.id.yes_button);
        Button maybeButton = (Button) view.findViewById(R.id.maybeButton);
        Button thanksButton = (Button) view.findViewById(R.id.thanksButton);

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse(Constants.GP_MARKET + Constants.APP_NAME)));
                AppUtils.appRated(context);
                dismiss();
            }
        });

        maybeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        thanksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppUtils.appRated(context);
                dismiss();
            }
        });
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
