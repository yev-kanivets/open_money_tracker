package com.blogspot.e_kanivets.moneytracker.activity.record;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v7.widget.AppCompatSpinner;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.format.DateFormat;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.activity.base.BaseBackActivity;
import com.blogspot.e_kanivets.moneytracker.adapter.CategoryAutoCompleteAdapter;
import com.blogspot.e_kanivets.moneytracker.controller.FormatController;
import com.blogspot.e_kanivets.moneytracker.controller.data.AccountController;
import com.blogspot.e_kanivets.moneytracker.controller.data.CategoryController;
import com.blogspot.e_kanivets.moneytracker.controller.data.RecordController;
import com.blogspot.e_kanivets.moneytracker.entity.data.Account;
import com.blogspot.e_kanivets.moneytracker.entity.data.Category;
import com.blogspot.e_kanivets.moneytracker.entity.data.Record;
import com.blogspot.e_kanivets.moneytracker.util.CategoryAutoCompleter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created on 1/26/16.
 *
 * @author Evgenii Kanivets
 */
public class AddRecordActivity extends BaseBackActivity {
    @SuppressWarnings("unused")
    private static final String TAG = "AddRecordActivity";

    public static final String KEY_RECORD = "key_record";
    public static final String KEY_MODE = "key_mode";
    public static final String KEY_TYPE = "key_type";

    private Record record;
    private Mode mode;
    private int type;

    private List<Account> accountList;
    private long timestamp;
    @StyleRes
    private int dialogTheme;

    @Inject
    CategoryController categoryController;
    @Inject
    RecordController recordController;
    @Inject
    AccountController accountController;
    @Inject
    FormatController formatController;

    @Bind(R.id.tv_date)
    TextView tvDate;
    @Bind(R.id.tv_time)
    TextView tvTime;
    @Bind(R.id.et_title)
    EditText etTitle;
    @Bind(R.id.et_category)
    AutoCompleteTextView etCategory;
    @Bind(R.id.et_price)
    EditText etPrice;
    @Bind(R.id.spinner_account)
    AppCompatSpinner spinnerAccount;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_add_record;
    }

    @Override
    protected boolean initData() {
        super.initData();
        getAppComponent().inject(AddRecordActivity.this);

        record = getIntent().getParcelableExtra(KEY_RECORD);
        mode = (Mode) getIntent().getSerializableExtra(KEY_MODE);
        type = getIntent().getIntExtra(KEY_TYPE, -1);
        accountList = accountController.readAll();

        if (mode == Mode.MODE_EDIT) timestamp = record.getTime();
        else timestamp = new Date().getTime();

        return mode != null && type != -1 && (!mode.equals(Mode.MODE_EDIT) || record != null);
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("SetTextI18n")
    @Override
    protected void initViews() {
        super.initViews();

        // Add texts to dialog if it's edit dialog
        if (mode == Mode.MODE_EDIT) {
            etTitle.setText(record.getTitle());
            if (record.getCategory() != null) etCategory.setText(record.getCategory().getName());
            etPrice.setText(formatController.formatPrecisionNone(record.getFullPrice()));
        }

        presentSpinnerAccount();

        dialogTheme = 0;
        if (getSupportActionBar() != null) {
            switch (type) {
                case Record.TYPE_EXPENSE:
                    getSupportActionBar().setTitle(R.string.title_add_expense);
                    getSupportActionBar().setBackgroundDrawable(
                            new ColorDrawable(getResources().getColor(R.color.red_light)));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Window window = getWindow();
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.setStatusBarColor(getResources().getColor(R.color.red_dark));
                        dialogTheme = R.style.RedDialogTheme;
                    }
                    break;

                case Record.TYPE_INCOME:
                    getSupportActionBar().setTitle(R.string.title_add_income);
                    getSupportActionBar().setBackgroundDrawable(
                            new ColorDrawable(getResources().getColor(R.color.green_light)));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Window window = getWindow();
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.setStatusBarColor(getResources().getColor(R.color.green_dark));
                        dialogTheme = R.style.GreenDialogTheme;
                    }
                    break;

                default:
                    break;
            }
        }

        etCategory.setAdapter(new CategoryAutoCompleteAdapter(AddRecordActivity.this,
                R.layout.view_category_item, new CategoryAutoCompleter(categoryController)));
        etCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                etCategory.setText((String) parent.getAdapter().getItem(position));
                etCategory.setSelection(etCategory.getText().length());
            }
        });
        etCategory.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) tryRecord();
                return false;
            }
        });

        // Restrict ';' for input, because it's used as delimiter when exporting
        etTitle.setFilters(new InputFilter[]{new SemicolonInputFilter()});
        etCategory.setFilters(new InputFilter[]{new SemicolonInputFilter()});

        updateDateAndTime();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_record, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        switch (mode) {
            case MODE_ADD:
                menu.removeItem(R.id.action_delete);
                break;

            case MODE_EDIT:
                break;

            default:
                break;
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                tryRecord();
                return true;

            case R.id.action_delete:
                recordController.delete(record);
                setResult(RESULT_OK);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @OnClick(R.id.tv_date)
    public void selectDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        DatePickerDialog dialog = new DatePickerDialog(AddRecordActivity.this, dialogTheme,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(timestamp);
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        if (calendar.getTimeInMillis() < new Date().getTime()) {
                            timestamp = calendar.getTimeInMillis();
                            updateDateAndTime();
                        }
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    @OnClick(R.id.tv_time)
    public void selectTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        TimePickerDialog dialog = new TimePickerDialog(AddRecordActivity.this, dialogTheme,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(timestamp);
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);

                        if (calendar.getTimeInMillis() < new Date().getTime()) {
                            timestamp = calendar.getTimeInMillis();
                            updateDateAndTime();
                        }
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
                DateFormat.is24HourFormat(AddRecordActivity.this));
        dialog.show();
    }

    private void tryRecord() {
        if (prepareRecord()) {
            setResult(RESULT_OK);
            finish();
        } else showToast(R.string.wrong_number_text);
    }

    private void presentSpinnerAccount() {
        List<String> accounts = new ArrayList<>();
        for (Account account : accountList) {
            accounts.add(account.getTitle());
        }

        int selectedAccountIndex = -1;

        if (mode == Mode.MODE_EDIT) {
            if (record.getAccount() != null) {
                for (int i = 0; i < accountList.size(); i++) {
                    Account account = accountList.get(i);
                    if (account.getId() == record.getAccount().getId()) selectedAccountIndex = i;
                }
            }
        } else if (mode == Mode.MODE_ADD) {
            Account defaultAccount = accountController.readDefaultAccount();
            if (defaultAccount != null) {
                for (int i = 0; i < accountList.size(); i++) {
                    Account account = accountList.get(i);
                    if (account.getId() == defaultAccount.getId()) selectedAccountIndex = i;
                }
            }
        }

        if (selectedAccountIndex == -1) {
            selectedAccountIndex = 0;
            spinnerAccount.setEnabled(false);

            accounts = new ArrayList<>();
            accounts.add(getString(R.string.account_was_removed));
        }

        spinnerAccount.setAdapter(new ArrayAdapter<>(AddRecordActivity.this,
                R.layout.view_spinner_item, accounts));
        spinnerAccount.setSelection(selectedAccountIndex);
    }

    private boolean prepareRecord() {
        String title = etTitle.getText().toString().trim();
        String category = etCategory.getText().toString().trim();

        if (title.isEmpty()) return false;
        if (category.isEmpty()) return false;

        //Check if price is valid
        double price = -1;
        try {
            price = Double.parseDouble(etPrice.getText().toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        long now = new Date().getTime();
        if (timestamp > now) return false;

        if (price < 0.0 || price > 1000000000.0) return false;

        if (spinnerAccount.getSelectedItemPosition() < 0) return false;

        Account account = null;
        if (spinnerAccount.isEnabled())
            account = accountList.get(spinnerAccount.getSelectedItemPosition());

        return doRecord(timestamp, title, category, price, account);
    }

    private boolean doRecord(long timestamp, String title, String category, double price,
                             @Nullable Account account) {
        if (account == null) return false;

        if (mode == Mode.MODE_ADD) {
            switch (type) {
                case Record.TYPE_EXPENSE:
                    recordController.create(new Record(timestamp, Record.TYPE_EXPENSE, title,
                            new Category(category), price, account, account.getCurrency()));
                    return true;

                case Record.TYPE_INCOME:
                    recordController.create(new Record(timestamp, Record.TYPE_INCOME, title,
                            new Category(category), price, account, account.getCurrency()));
                    return true;

                default:
                    return false;
            }
        } else if (mode == Mode.MODE_EDIT) {
            Record updatedRecord = new Record(record.getId(), timestamp, record.getType(),
                    title, new Category(category), price, account, account.getCurrency());
            recordController.update(updatedRecord);

            return true;
        }

        return false;
    }

    private void updateDateAndTime() {
        tvDate.setText(formatController.formatDate(timestamp));
        tvTime.setText(formatController.formatTime(timestamp));
    }

    public enum Mode {MODE_ADD, MODE_EDIT}

    private static class SemicolonInputFilter implements InputFilter {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            if (source != null && ";".equals(source.toString())) return "";
            else return null;
        }
    }
}
