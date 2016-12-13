package com.blogspot.e_kanivets.moneytracker.util.validator;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.activity.record.AddRecordActivity;
import com.blogspot.e_kanivets.moneytracker.entity.data.Account;
import com.blogspot.e_kanivets.moneytracker.entity.data.Category;
import com.blogspot.e_kanivets.moneytracker.entity.data.Record;

import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Util class for Account validation.
 * Created on 06.12.2016.
 *
 * @author Evgenii Kanivets
 */

public class RecordValidator implements IValidator<Record> {

    @NonNull
    private final Context context;

    private AddRecordActivity.Mode mode;
    private List<Account> accountList;
    private long timestamp;
    private long recordId;
    private int recordType;

    @Bind(R.id.til_title)
    TextInputLayout tilTitle;
    @Bind(R.id.et_title)
    EditText etTitle;
    @Bind(R.id.til_category)
    TextInputLayout tilCategory;
    @Bind(R.id.et_category)
    EditText etCategory;
    @Bind(R.id.til_price)
    TextInputLayout tilPrice;
    @Bind(R.id.et_price)
    EditText etPrice;
    @Bind(R.id.spinner_account)
    AppCompatSpinner spinnerAccount;

    public RecordValidator(@NonNull Context context, @NonNull View view,
                           @NonNull AddRecordActivity.Mode mode, @NonNull List<Account> accountList,
                           long timestamp, int recordType, long recordId) {
        this.context = context;
        this.mode = mode;
        this.accountList = accountList;
        this.timestamp = timestamp;
        this.recordType = recordType;
        this.recordId = recordId;

        ButterKnife.bind(this, view);
        initTextWatchers();
    }

    @Nullable
    @Override
    public Record validate() {
        boolean valid = true;

        String title = etTitle.getText().toString().trim();
        String category = etCategory.getText().toString().trim();

        if (title.isEmpty()) {
            title = category;
        }

        if (category.isEmpty()) {
            tilCategory.setError(context.getString(R.string.field_cant_be_empty));
            valid = false;
        }

        //Check if price is valid
        double price = Double.MAX_VALUE;
        try {
            price = Double.parseDouble(etPrice.getText().toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        if (price == Double.MAX_VALUE) {
            tilPrice.setError(context.getString(R.string.field_cant_be_empty));
            price = 0;
            valid = false;
        }

        if (price > MAX_ABS_VALUE) {
            tilPrice.setError(context.getString(R.string.too_rich));
            valid = false;
        }

        long now = new Date().getTime();
        if (timestamp > now) {
            Toast.makeText(context, R.string.record_in_future, Toast.LENGTH_SHORT).show();
            valid = false;
        }

        Account account = null;
        if (spinnerAccount.isEnabled()) {
            account = accountList.get(spinnerAccount.getSelectedItemPosition());
        } else {
            Toast.makeText(context, R.string.one_account_needed, Toast.LENGTH_SHORT).show();
            valid = false;
        }

        Record record = null;
        if (account != null) {
            if (mode == AddRecordActivity.Mode.MODE_ADD) {
                record = new Record(timestamp, recordType, title, new Category(category),
                        price, account, account.getCurrency());
            } else if (mode == AddRecordActivity.Mode.MODE_EDIT) {
                record = new Record(recordId, timestamp, recordType, title, new Category(category),
                        price, account, account.getCurrency());
            }
        }

        return valid ? record : null;
    }

    private void initTextWatchers() {
        etPrice.addTextChangedListener(new ClearErrorTextWatcher(tilPrice));
        etTitle.addTextChangedListener(new ClearErrorTextWatcher(tilTitle));
        etCategory.addTextChangedListener(new ClearErrorTextWatcher(tilCategory));
    }
}
