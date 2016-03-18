package com.blogspot.e_kanivets.moneytracker.activity.record;

import android.annotation.SuppressLint;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.activity.base.BaseBackActivity;
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

    protected Record record;
    protected Mode mode;
    protected int type;

    protected List<Account> accountList;

    protected AccountController accountController;
    protected RecordController recordController;

    @Bind(R.id.et_title)
    EditText etTitle;
    @Bind(R.id.et_category)
    EditText etCategory;
    @Bind(R.id.et_price)
    EditText etPrice;
    @Bind(R.id.spinner_account)
    Spinner spinnerAccount;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_add_record;
    }

    @Override
    protected boolean initData() {
        super.initData();

        DbHelper dbHelper = new DbHelper(AddRecordActivity.this);

        AccountRepo accountRepo = new AccountRepo(dbHelper);
        accountController = new AccountController(accountRepo);
        recordController = new RecordController(new RecordRepo(dbHelper),
                new CategoryController(new CategoryRepo(dbHelper)), new AccountController(accountRepo));

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
                if (prepareRecord()) {
                    setResult(RESULT_OK);
                    finish();
                } else showToast(R.string.wrong_number_text);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
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
                R.layout.view_account_item, accounts));
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