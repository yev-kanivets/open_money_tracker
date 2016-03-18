package com.blogspot.e_kanivets.moneytracker.activity.account;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.blogspot.e_kanivets.moneytracker.DbHelper;
import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.activity.base.BaseBackActivity;
import com.blogspot.e_kanivets.moneytracker.adapter.AccountAdapter;
import com.blogspot.e_kanivets.moneytracker.controller.AccountController;
import com.blogspot.e_kanivets.moneytracker.repo.AccountRepo;
import com.blogspot.e_kanivets.moneytracker.ui.AccountsSummaryPresenter;

import butterknife.Bind;
import butterknife.OnClick;

public class AccountsActivity extends BaseBackActivity {
    @SuppressWarnings("unused")
    private static final String TAG = "AccountsActivity";

    private static final int REQUEST_ADD_ACCOUNT = 1;
    private static final int REQUEST_TRANSFER = 2;

    private AccountController accountController;
    private AccountsSummaryPresenter summaryPresenter;

    @Bind(R.id.list_view)
    ListView listView;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_accounts;
    }

    @Override
    protected boolean initData() {
        DbHelper dbHelper = new DbHelper(AccountsActivity.this);
        accountController = new AccountController(new AccountRepo(dbHelper));
        summaryPresenter = new AccountsSummaryPresenter(AccountsActivity.this);
        return super.initData();
    }

    @Override
    protected void initViews() {
        super.initViews();

        listView.addHeaderView(summaryPresenter.create());

        registerForContextMenu(listView);
        update();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_accounts, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_transfer:
                startActivityForResult(new Intent(AccountsActivity.this, TransferActivity.class),
                        REQUEST_TRANSFER);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu_account, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
            case R.id.delete:
                // Minus one because of list view's header view
                accountController.delete(accountController.readAll().get(info.position - 1));
                update();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @OnClick(R.id.btn_add_account)
    public void addAccount() {
        Intent intent = new Intent(AccountsActivity.this, AddAccountActivity.class);
        startActivityForResult(intent, REQUEST_ADD_ACCOUNT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == AppCompatActivity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_ADD_ACCOUNT:
                    update();
                    break;

                case REQUEST_TRANSFER:
                    update();
                    break;

                default:
                    break;
            }
        }
    }

    private void update() {
        listView.setAdapter(new AccountAdapter(AccountsActivity.this, accountController.readAll()));
        summaryPresenter.update();
    }
}
