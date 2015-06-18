package com.blogspot.e_kanivets.moneytracker.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.activity.NavDrawerActivity;
import com.blogspot.e_kanivets.moneytracker.helper.MTHelper;
import com.blogspot.e_kanivets.moneytracker.model.Account;
import com.blogspot.e_kanivets.moneytracker.model.Record;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddExpenseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddExpenseFragment extends Fragment {
    public static final String TAG = "AddExpenseFragment";

    private static final String KEY_RECORD = "key_record";
    private static final String KEY_MODE = "key_mode";

    private Record record;
    private Mode mode;

    private View rootView;
    private EditText etTitle;
    private EditText etCategory;
    private EditText etPrice;
    private Spinner spinnerAccount;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AddExpenseFragment.
     */
    public static AddExpenseFragment newInstance(Record record, Mode mode) {
        AddExpenseFragment fragment = new AddExpenseFragment();
        Bundle args = new Bundle();
        args.putSerializable(KEY_RECORD, record);
        args.putSerializable(KEY_MODE, mode);
        fragment.setArguments(args);
        return fragment;
    }

    public AddExpenseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            record = (Record) getArguments().get(KEY_RECORD);
            mode = (Mode) getArguments().get(KEY_MODE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_add_record, container, false);
        initViews(rootView);
        initActionBar();
        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        ((NavDrawerActivity) activity).onSectionAttached(TAG);
    }

    @Override
    public void onDetach() {
        /* hide keyboard */
        ((InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE))
                .toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
        super.onDetach();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_add_record, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                String title = etTitle.getText().toString();
                String category = etCategory.getText().toString();

                //Check if price is valid
                int price = 0;
                try {
                    price = Integer.parseInt(etPrice.getText().toString());
                    if (price >= 0 && price <= 1000000000) {
                        Account account = MTHelper.getInstance().getAccounts().get(spinnerAccount.getSelectedItemPosition());

                        if (mode == Mode.MODE_ADD) {
                            MTHelper.getInstance().addRecord(new Date().getTime(), 1, title, category,
                                    price, account.getId(), -price);
                        }
                        if (mode == Mode.MODE_EDIT) {
                            MTHelper.getInstance().updateRecordById(record.getId(), title, category,
                                    price, account.getId(), -(price - record.getPrice()));
                        }

                    } else {
                        throw new NumberFormatException();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.wrong_number_text),
                            Toast.LENGTH_SHORT).show();
                }

                finish();
                return true;

            case R.id.action_close:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initViews(final View rootView) {
        if (rootView != null) {
            etTitle = (EditText) rootView.findViewById(R.id.et_title);
            etCategory = (EditText) rootView.findViewById(R.id.et_category);
            etPrice = (EditText) rootView.findViewById(R.id.et_price);

            List<String> accounts = new ArrayList<>();
            for (Account account : MTHelper.getInstance().getAccounts()) {
                accounts.add(account.getTitle());
            }

            spinnerAccount = (Spinner) rootView.findViewById(R.id.spinner_account);
            spinnerAccount.setAdapter(new ArrayAdapter<>(getActivity(),
                    android.R.layout.simple_list_item_1, accounts));

            //Add texts to dialog if it's edit dialog
            if (mode == Mode.MODE_EDIT) {
                etTitle.setText(record.getTitle());
                etCategory.setText(record.getCategory());
                etPrice.setText(Integer.toString(record.getPrice()));

                for (int i = 0; i < MTHelper.getInstance().getAccounts().size(); i++) {
                    Account account = MTHelper.getInstance().getAccounts().get(i);
                    if (account.getId() == record.getAccountId()) {
                        spinnerAccount.setSelection(i);
                    }
                }
            }

            /* Show keyboard and set focus on etTitle */
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            etTitle.requestFocus();
        }
    }

    private void initActionBar() {
        ActionBar actionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setCustomView(null);
        }
    }

    private void finish() {
        getActivity().getSupportFragmentManager().popBackStack();
    }

    public enum Mode {MODE_ADD, MODE_EDIT}
}