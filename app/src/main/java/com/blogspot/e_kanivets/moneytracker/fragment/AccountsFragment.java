package com.blogspot.e_kanivets.moneytracker.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.activity.AddAccountActivity;
import com.blogspot.e_kanivets.moneytracker.activity.NavDrawerActivity;
import com.blogspot.e_kanivets.moneytracker.activity.TransferActivity;
import com.blogspot.e_kanivets.moneytracker.adapter.AccountAdapter;
import com.blogspot.e_kanivets.moneytracker.DbHelper;
import com.blogspot.e_kanivets.moneytracker.controller.AccountController;
import com.blogspot.e_kanivets.moneytracker.model.Account;
import com.blogspot.e_kanivets.moneytracker.repo.AccountRepo;
import com.blogspot.e_kanivets.moneytracker.repo.IRepo;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AccountsFragment extends Fragment {
    public static final String TAG = "AccountsFragment";

    private static final int REQUEST_ADD_ACCOUNT = 1;
    private static final int REQUEST_TRANSFER = 2;

    @Bind(R.id.list_view)
    ListView listView;

    private AccountController accountController;

    public static AccountsFragment newInstance() {
        AccountsFragment fragment = new AccountsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public AccountsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        accountController = new AccountController(new AccountRepo(new DbHelper(getActivity())));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_accounts, container, false);
        initViews(rootView);
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_accounts, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_transfer:
                startActivityForResult(new Intent(getActivity(), TransferActivity.class),
                        REQUEST_TRANSFER);
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        initActionBar();

        ((NavDrawerActivity) activity).onSectionAttached(TAG);
    }

    @OnClick(R.id.btn_add_account)
    public void addAccount() {
        Intent intent = new Intent(getActivity(), AddAccountActivity.class);
        startActivityForResult(intent, REQUEST_ADD_ACCOUNT);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.menu_account, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
            case R.id.delete:
                accountController.delete(accountController.readAll().get(info.position));
                update();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
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
        listView.setAdapter(new AccountAdapter(getActivity(), accountController.readAll()));
        ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
    }

    private void initViews(View rootView) {
        if (rootView != null) {
            ButterKnife.bind(this, rootView);

            listView.setAdapter(new AccountAdapter(getActivity(), accountController.readAll()));
            ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
            registerForContextMenu(listView);

            ((NavDrawerActivity) getActivity()).onSectionAttached(TAG);
        }
    }

    private void initActionBar() {
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) actionBar.setCustomView(null);
    }
}