package com.blogspot.e_kanivets.moneytracker.util.validator;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.EditText;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.entity.data.Account;
import com.blogspot.e_kanivets.moneytracker.entity.data.Transfer;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Util class for Transfer validation.
 * Created on 07.12.2016.
 *
 * @author Evgenii Kanivets
 */

@SuppressWarnings("WeakerAccess")
public class TransferValidator implements IValidator<Transfer> {

    @NonNull
    private final Context context;

    @NonNull
    private List<Account> accountList;

    @Bind(R.id.spinner_from)
    AppCompatSpinner spinnerFrom;
    @Bind(R.id.spinner_to)
    AppCompatSpinner spinnerTo;
    @Bind(R.id.til_from_amount)
    TextInputLayout tilFromAmount;
    @Bind(R.id.et_from_amount)
    EditText etFromAmount;
    @Bind(R.id.til_to_amount)
    TextInputLayout tilToAmount;
    @Bind(R.id.et_to_amount)
    EditText etToAmount;

    public TransferValidator(@NonNull Context context, @NonNull View view,
                             @NonNull List<Account> accountList) {
        this.context = context;
        this.accountList = accountList;
        ButterKnife.bind(this, view);
        initTextWatchers();
    }

    @Nullable
    @Override
    public Transfer validate() {
        boolean valid = true;

        Account fromAccount = null;
        if (spinnerFrom.isEnabled()) {
            fromAccount = accountList.get(spinnerFrom.getSelectedItemPosition());
        } else {
            valid = false;
        }

        Account toAccount = null;
        if (spinnerTo.isEnabled()) {
            toAccount = accountList.get(spinnerTo.getSelectedItemPosition());
        } else {
            valid = false;
        }

        double fromAmount = Double.MAX_VALUE;
        try {
            fromAmount = Double.parseDouble(etFromAmount.getText().toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        if (fromAmount == Double.MAX_VALUE) {
            tilFromAmount.setError(context.getString(R.string.field_cant_be_empty));
            fromAmount = 0;
            valid = false;
        }

        if (fromAmount > MAX_ABS_VALUE) {
            tilFromAmount.setError(context.getString(R.string.too_much_for_transfer));
            valid = false;
        }

        double toAmount = Double.MAX_VALUE;
        try {
            toAmount = Double.parseDouble(etToAmount.getText().toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        if (toAmount == Double.MAX_VALUE) {
            tilToAmount.setError(context.getString(R.string.field_cant_be_empty));
            toAmount = 0;
            valid = false;
        }

        if (toAmount > MAX_ABS_VALUE) {
            tilToAmount.setError(context.getString(R.string.too_much_for_transfer));
            valid = false;
        }

        return valid ? new Transfer(System.currentTimeMillis(), fromAccount.getId(),
                toAccount.getId(), fromAmount, toAmount) : null;
    }

    private void initTextWatchers() {
        etFromAmount.addTextChangedListener(new ClearErrorTextWatcher(tilFromAmount));
        etToAmount.addTextChangedListener(new ClearErrorTextWatcher(tilToAmount));
    }
}
