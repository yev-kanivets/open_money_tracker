package com.blogspot.e_kanivets.moneytracker.util.validator;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.EditText;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.entity.data.Account;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Util class for Account validation.
 * Created on 06.12.2016.
 *
 * @author Evgenii Kanivets
 */

public class AccountValidator implements IValidator<Account> {

    @NonNull
    private final Context context;

    @Bind(R.id.til_title)
    TextInputLayout tilTitle;
    @Bind(R.id.et_title)
    EditText etTitle;
    @Bind(R.id.til_init_sum)
    TextInputLayout tilInitSum;
    @Bind(R.id.et_init_sum)
    EditText etInitSum;
    @Bind(R.id.spinner)
    AppCompatSpinner spinner;

    public AccountValidator(@NonNull Context context, @NonNull View view) {
        this.context = context;
        ButterKnife.bind(this, view);
        initTextWatchers();
    }

    @Override
    public boolean validate() {
        String title = etTitle.getText().toString().trim();
        double initSum = Double.MAX_VALUE;

        try {
            initSum = Double.parseDouble(etInitSum.getText().toString().trim());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        boolean valid = true;

        if (title.isEmpty()) {
            tilTitle.setError(context.getString(R.string.field_cant_be_empty));
            valid = false;
        }

        if (initSum == Double.MAX_VALUE) {
            tilInitSum.setError(context.getString(R.string.field_cant_be_empty));
            initSum = 0;
            valid = false;
        }

        if (Math.abs(initSum) > MAX_ABS_VALUE) {
            tilInitSum.setError(context.getString(R.string.too_rich_or_poor));
            valid = false;
        }

        return valid;
    }

    private void initTextWatchers() {
        etTitle.addTextChangedListener(new ClearErrorTextWatcher(tilTitle));
        etInitSum.addTextChangedListener(new ClearErrorTextWatcher(tilInitSum));
    }
}
