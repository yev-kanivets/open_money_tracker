package com.blogspot.e_kanivets.moneytracker.activity.account;

import android.support.v7.widget.AppCompatSpinner;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.activity.base.BaseBackActivity;
import com.blogspot.e_kanivets.moneytracker.controller.data.AccountController;
import com.blogspot.e_kanivets.moneytracker.controller.data.TransferController;
import com.blogspot.e_kanivets.moneytracker.entity.data.Account;
import com.blogspot.e_kanivets.moneytracker.entity.data.Transfer;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;

public class TransferActivity extends BaseBackActivity {
    @SuppressWarnings("unused")
    private static final String TAG = "TransferActivity";

    @Inject
    TransferController transferController;
    @Inject
    AccountController accountController;

    private List<Account> accountList;

    @Bind(R.id.spinner_from)
    AppCompatSpinner spinnerFrom;
    @Bind(R.id.spinner_to)
    AppCompatSpinner spinnerTo;
    @Bind(R.id.et_from_amount)
    EditText etFromAmount;
    @Bind(R.id.et_to_amount)
    EditText etToAmount;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_transfer;
    }

    @Override
    protected boolean initData() {
        boolean result = super.initData();
        getAppComponent().inject(TransferActivity.this);
        accountList = accountController.readAll();
        return result;
    }

    @Override
    protected void initViews() {
        super.initViews();

        List<String> accounts = new ArrayList<>();
        for (Account account : accountList) {
            accounts.add(account.getTitle());
        }

        spinnerFrom.setAdapter(new ArrayAdapter<>(TransferActivity.this,
                R.layout.view_spinner_item, accounts));

        spinnerTo.setAdapter(new ArrayAdapter<>(TransferActivity.this,
                R.layout.view_spinner_item, accounts));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_transfer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                doTransfer();

                setResult(RESULT_OK);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void doTransfer() {
        Account fromAccount = accountList.get(spinnerFrom.getSelectedItemPosition());
        Account toAccount = accountList.get(spinnerTo.getSelectedItemPosition());

        int fromAmount = -1;
        try {
            fromAmount = Integer.parseInt(etFromAmount.getText().toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        int toAmount = -1;
        try {
            toAmount = Integer.parseInt(etToAmount.getText().toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        Transfer transfer = new Transfer(System.currentTimeMillis(), fromAccount.getId(),
                toAccount.getId(), fromAmount, toAmount, 0, 0);
        transferController.create(transfer);
    }
}
