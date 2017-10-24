package com.blogspot.e_kanivets.moneytracker.activity.account;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.activity.base.BaseBackActivity;
import com.blogspot.e_kanivets.moneytracker.controller.data.AccountController;
import com.blogspot.e_kanivets.moneytracker.entity.data.Account;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

public class EditAccountActivity extends BaseBackActivity {

    public static final String KEY_ACCOUNT = "key_account";

    @Inject
    AccountController accountController;

    private Account account;

    @Bind(R.id.et_title)
    EditText etTitle;
    @Bind(R.id.et_goal)
    EditText etGoal;
    @Bind(R.id.view_color)
    View viewColor;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_edit_account;
    }

    @Override
    protected boolean initData() {
        getAppComponent().inject(EditAccountActivity.this);
        account = getIntent().getParcelableExtra(KEY_ACCOUNT);
        return account != null && super.initData();
    }

    @Override
    protected void initViews() {
        super.initViews();

        etTitle.setText(account.getTitle());
        etGoal.setText(Double.toString(account.getGoal()));
        viewColor.setBackgroundColor(account.getColor());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (account.isArchived()) {
            getMenuInflater().inflate(R.menu.menu_archived_account, menu);
        } else {
            getMenuInflater().inflate(R.menu.menu_account, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_archive:
                archive();
                return true;

            case R.id.action_restore:
                restore();
                return true;

            case R.id.action_delete:
                delete();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @OnClick(R.id.fabDone)
    void done() {

    }

    private void archive() {
        if (account.equals(accountController.readDefaultAccount())) {
            showToast("You can't archive a default account.");
        } else {
            accountController.archive(account);
            setResult(RESULT_OK);
            finish();
        }
    }

    private void restore() {
        accountController.restore(account);
        setResult(RESULT_OK);
        finish();
    }

    private void delete() {
        accountController.delete(account);
        setResult(RESULT_OK);
        finish();
    }

}
