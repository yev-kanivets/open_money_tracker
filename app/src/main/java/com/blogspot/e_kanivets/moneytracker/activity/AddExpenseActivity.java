package com.blogspot.e_kanivets.moneytracker.activity;

import android.annotation.SuppressLint;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.controller.AccountController;
import com.blogspot.e_kanivets.moneytracker.controller.RecordController;
import com.blogspot.e_kanivets.moneytracker.helper.DbHelper;
import com.blogspot.e_kanivets.moneytracker.model.Account;
import com.blogspot.e_kanivets.moneytracker.model.Record;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;

public class AddExpenseActivity extends BaseActivity {
    @SuppressWarnings("unused")
    private static final String TAG = "AddExpenseActivity";

    public static final String KEY_RECORD = "key_record";
    public static final String KEY_MODE = "key_mode";

    private Record record;
    private Mode mode;

    @Bind(R.id.et_title)
    EditText etTitle;
    @Bind(R.id.et_category)
    EditText etCategory;
    @Bind(R.id.et_price)
    EditText etPrice;
    @Bind(R.id.spinner_account)
    Spinner spinnerAccount;

    private RecordController recordController;
    private AccountController accountController;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_add_record;
    }

    @Override
    protected boolean initData() {
        super.initData();

        recordController = new RecordController(new DbHelper(AddExpenseActivity.this));
        accountController = new AccountController(new DbHelper(AddExpenseActivity.this));

        record = (Record) getIntent().getSerializableExtra(KEY_RECORD);
        mode = (Mode) getIntent().getSerializableExtra(KEY_MODE);

        return mode != null && (!mode.equals(Mode.MODE_EDIT) || record != null);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initViews() {
        super.initViews();

        List<Account> accountList = accountController.getAccounts();

        List<String> accounts = new ArrayList<>();
        for (Account account : accountList) {
            accounts.add(account.getTitle());
        }

        spinnerAccount = (Spinner) findViewById(R.id.spinner_account);
        spinnerAccount.setAdapter(new ArrayAdapter<>(AddExpenseActivity.this,
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
                if (doExpense()) {
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

    private boolean doExpense() {
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

        if (price >= 0 && price <= 1000000000) {
            Account account = accountController.getAccounts().get(spinnerAccount.getSelectedItemPosition());

            if (mode == Mode.MODE_ADD) recordController.addRecord(new Date().getTime(),
                    1, title, category, price, account.getId(), -price);
            if (mode == Mode.MODE_EDIT)
                recordController.updateRecordById(record.getId(),
                        title, category, price, account.getId(), -(price - record.getPrice()));
        } else return false;

        return true;
    }

    public enum Mode {MODE_ADD, MODE_EDIT}
}