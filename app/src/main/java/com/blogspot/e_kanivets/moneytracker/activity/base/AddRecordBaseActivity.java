package com.blogspot.e_kanivets.moneytracker.activity.base;

import android.annotation.SuppressLint;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.controller.AccountController;
import com.blogspot.e_kanivets.moneytracker.controller.CategoryController;
import com.blogspot.e_kanivets.moneytracker.DbHelper;
import com.blogspot.e_kanivets.moneytracker.model.Account;
import com.blogspot.e_kanivets.moneytracker.model.Record;
import com.blogspot.e_kanivets.moneytracker.repo.AccountRepo;
import com.blogspot.e_kanivets.moneytracker.repo.CategoryRepo;
import com.blogspot.e_kanivets.moneytracker.repo.IRepo;
import com.blogspot.e_kanivets.moneytracker.repo.RecordRepo;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Base implementation of {@link android.support.v7.app.AppCompatActivity} to describe some common
 * methods which connected with income's/expense's handling.
 * Created on 1/26/16.
 *
 * @author Evgenii Kanivets
 */
public abstract class AddRecordBaseActivity extends BaseActivity {
    @SuppressWarnings("unused")
    private static final String TAG = "AddRecordBaseActivity";

    public static final String KEY_RECORD = "key_record";
    public static final String KEY_MODE = "key_mode";

    protected Record record;
    protected Mode mode;

    protected List<Account> accountList;

    protected IRepo<Account> accountRepo;
    protected IRepo<Record> recordRepo;

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

        DbHelper dbHelper = new DbHelper(AddRecordBaseActivity.this);

        accountRepo = new AccountRepo(dbHelper);
        recordRepo = new RecordRepo(dbHelper, new AccountController(accountRepo),
                new CategoryController(new CategoryRepo(dbHelper)));

        record = (Record) getIntent().getSerializableExtra(KEY_RECORD);
        mode = (Mode) getIntent().getSerializableExtra(KEY_MODE);
        accountList = accountRepo.readAll();

        return mode != null && (!mode.equals(Mode.MODE_EDIT) || record != null);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initViews() {
        super.initViews();

        List<String> accounts = new ArrayList<>();
        for (Account account : accountList) {
            accounts.add(account.getTitle());
        }

        spinnerAccount.setAdapter(new ArrayAdapter<>(AddRecordBaseActivity.this,
                android.R.layout.simple_list_item_1, accounts));

        //Add texts to dialog if it's edit dialog
        if (mode == Mode.MODE_EDIT) {
            etTitle.setText(record.getTitle());
            etCategory.setText(record.getCategory());
            etPrice.setText(Integer.toString(record.getPrice()));

            for (int i = 0; i < accountList.size(); i++) {
                Account account = accountList.get(i);
                if (account.getId() == record.getAccountId()) {
                    spinnerAccount.setSelection(i);
                }
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

            case R.id.action_close:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected abstract boolean doRecord(String title, String category, int price, Account account);

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
            Account account = accountList.get(spinnerAccount.getSelectedItemPosition());
            return doRecord(title, category, price, account);
        } else return false;
    }

    public enum Mode {MODE_ADD, MODE_EDIT}
}