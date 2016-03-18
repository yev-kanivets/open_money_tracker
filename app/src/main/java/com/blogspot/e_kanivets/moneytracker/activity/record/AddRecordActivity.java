package com.blogspot.e_kanivets.moneytracker.activity.record;

import android.annotation.SuppressLint;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatSpinner;
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
import android.widget.EditText;
import android.widget.TextView;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.activity.base.BaseBackActivity;
import com.blogspot.e_kanivets.moneytracker.adapter.CategoryAutoCompleteAdapter;
import com.blogspot.e_kanivets.moneytracker.controller.AccountController;
import com.blogspot.e_kanivets.moneytracker.controller.CategoryController;
import com.blogspot.e_kanivets.moneytracker.DbHelper;
import com.blogspot.e_kanivets.moneytracker.controller.RecordController;
import com.blogspot.e_kanivets.moneytracker.entity.Account;
import com.blogspot.e_kanivets.moneytracker.entity.Category;
import com.blogspot.e_kanivets.moneytracker.entity.Record;
import com.blogspot.e_kanivets.moneytracker.repo.AccountRepo;
import com.blogspot.e_kanivets.moneytracker.repo.CategoryRepo;
import com.blogspot.e_kanivets.moneytracker.repo.RecordRepo;
import com.blogspot.e_kanivets.moneytracker.util.CategoryAutoCompleter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;

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

    private CategoryController categoryController;
    private RecordController recordController;

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

        DbHelper dbHelper = new DbHelper(AddRecordActivity.this);

        AccountRepo accountRepo = new AccountRepo(dbHelper);
        AccountController accountController = new AccountController(accountRepo);
        categoryController = new CategoryController(new CategoryRepo(dbHelper));
        recordController = new RecordController(new RecordRepo(dbHelper), categoryController,
                new AccountController(accountRepo));

        record = getIntent().getParcelableExtra(KEY_RECORD);
        mode = (Mode) getIntent().getSerializableExtra(KEY_MODE);
        type = getIntent().getIntExtra(KEY_TYPE, -1);
        accountList = accountController.readAll();

        return mode != null && type != -1 && (!mode.equals(Mode.MODE_EDIT) || record != null);
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("SetTextI18n")
    @Override
    protected void initViews() {
        super.initViews();

        //Add texts to dialog if it's edit dialog
        if (mode == Mode.MODE_EDIT) {
            etTitle.setText(record.getTitle());
            if (record.getCategory() != null) etCategory.setText(record.getCategory().getName());
            etPrice.setText(Integer.toString(record.getPrice()));
        }

        presentSpinnerAccount();

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_record, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                tryRecord();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
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

        int selectedAccountIndex = 0;

        if (mode == Mode.MODE_EDIT) {
            selectedAccountIndex = -1;

            if (record.getAccount() != null) {
                for (int i = 0; i < accountList.size(); i++) {
                    Account account = accountList.get(i);
                    if (account.getId() == record.getAccount().getId()) selectedAccountIndex = i;
                }
            }

            if (selectedAccountIndex == -1) {
                selectedAccountIndex = 0;
                spinnerAccount.setEnabled(false);

                accounts = new ArrayList<>();
                accounts.add(getString(R.string.account_was_removed));
            }
        }

        spinnerAccount.setAdapter(new ArrayAdapter<>(AddRecordActivity.this,
                R.layout.view_spinner_item, accounts));
        spinnerAccount.setSelection(selectedAccountIndex);
    }

    private boolean prepareRecord() {
        String title = etTitle.getText().toString().trim();
        String category = etCategory.getText().toString().trim();

        //Check if price is valid
        //noinspection UnusedAssignment
        int price = -1;
        try {
            price = Integer.parseInt(etPrice.getText().toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        if (price >= 0 && price <= 1000000000 && spinnerAccount.getSelectedItemPosition() >= 0) {
            Account account = null;
            if (spinnerAccount.isEnabled())
                account = accountList.get(spinnerAccount.getSelectedItemPosition());

            return doRecord(title, category, price, account);
        } else return false;
    }

    private boolean doRecord(String title, String category, int price, @Nullable Account account) {
        if (account == null) return false;

        if (mode == Mode.MODE_ADD) {
            switch (type) {
                case Record.TYPE_EXPENSE:
                    recordController.create(new Record(new Date().getTime(), Record.TYPE_EXPENSE,
                            title, new Category(category), price, account, account.getCurrency()));
                    return true;

                case Record.TYPE_INCOME:
                    recordController.create(new Record(new Date().getTime(), Record.TYPE_INCOME,
                            title, new Category(category), price, account, account.getCurrency()));
                    return true;

                default:
                    return false;
            }
        } else if (mode == Mode.MODE_EDIT) {
            Record updatedRecord = new Record(record.getId(), record.getTime(), record.getType(),
                    title, new Category(category), price, account, account.getCurrency());
            recordController.update(updatedRecord);

            return true;
        }

        return false;
    }

    public enum Mode {MODE_ADD, MODE_EDIT}
}