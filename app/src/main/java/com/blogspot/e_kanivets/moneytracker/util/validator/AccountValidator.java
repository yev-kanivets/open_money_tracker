package com.blogspot.e_kanivets.moneytracker.util.validator;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatSpinner;
import android.text.Editable;
import android.text.TextWatcher;
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

    @Nullable
    @Override
    public Account validate() {
        String title = etTitle.getText().toString().trim();
        double initSum = Double.MAX_VALUE;
        String currency = (String) spinner.getSelectedItem();

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
            tilInitSum.setError(context.getString(R.string.too_rich));
            valid = false;
        }

        return valid ? new Account(title, initSum, currency) : null;
    }

    private void initTextWatchers() {
        etTitle.addTextChangedListener(new ClearErrorTextWatcher(tilTitle));
        etInitSum.addTextChangedListener(new ClearErrorTextWatcher(tilInitSum));
    }
}
