package com.blogspot.e_kanivets.moneytracker.activity.account;

import android.support.v7.widget.AppCompatSpinner;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.activity.base.BaseBackActivity;
import com.blogspot.e_kanivets.moneytracker.controller.data.AccountController;
import com.blogspot.e_kanivets.moneytracker.controller.data.TransferController;
import com.blogspot.e_kanivets.moneytracker.entity.data.Account;
import com.blogspot.e_kanivets.moneytracker.entity.data.Transfer;
import com.blogspot.e_kanivets.moneytracker.util.validator.IValidator;
import com.blogspot.e_kanivets.moneytracker.util.validator.TransferValidator;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;

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

    private IValidator<Transfer> transferValidator;

    private List<Account> accountList;

    @Bind(R.id.content)
    View contentView;
    @Bind(R.id.spinner_from)
    AppCompatSpinner spinnerFrom;
    @Bind(R.id.spinner_to)
    AppCompatSpinner spinnerTo;

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

        transferValidator = new TransferValidator(TransferActivity.this, contentView, accountList);

        if (accounts.size() == 0) {
            accounts.add(getString(R.string.none));
            spinnerFrom.setEnabled(false);
            spinnerTo.setEnabled(false);
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
                tryTransfer();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void tryTransfer() {
        // Answers event
        Answers.getInstance().logContentView(new ContentViewEvent()
                .putContentName("Done Transfer")
                .putContentType("Button"));

        if (doTransfer()) {
            // Answers event
            Answers.getInstance().logContentView(new ContentViewEvent()
                    .putContentName("Done Transfer")
                    .putContentType("Event"));

            setResult(RESULT_OK);
            finish();
        }
    }

    @SuppressWarnings("SimplifiableIfStatement")
    private boolean doTransfer() {
        Transfer transfer = transferValidator.validate();
        if (transfer == null) return false;
        else return transferController.create(transfer) != null;
    }
}
