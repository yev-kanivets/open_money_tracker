package com.blogspot.e_kanivets.moneytracker.fragment;

import android.app.Activity;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.activity.NavDrawerActivity;
import com.blogspot.e_kanivets.moneytracker.helper.MTHelper;
import com.blogspot.e_kanivets.moneytracker.model.Record;

import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddIncomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddIncomeFragment extends Fragment {
    public static final String TAG = "AddIncomeFragment";

    private static final String KEY_RECORD = "key_record";
    private static final String KEY_MODE = "key_mode";

    private Record record;
    private Mode mode;

    private EditText etTitle;
    private EditText etCategory;
    private EditText etPrice;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AddIncomeFragment.
     */
    public static AddIncomeFragment newInstance(Record record, Mode mode) {
        AddIncomeFragment fragment = new AddIncomeFragment();
        Bundle args = new Bundle();
        args.putSerializable(KEY_RECORD, record);
        args.putSerializable(KEY_MODE, mode);
        fragment.setArguments(args);
        return fragment;
    }

    public AddIncomeFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_add_record, container, false);
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
                        if (mode == Mode.MODE_ADD) {
                            MTHelper.getInstance().addRecord(new Date().getTime(), 0, title, category, price);
                        }
                        if (mode == Mode.MODE_EDIT) {
                            MTHelper.getInstance().updateRecordById(record.getId(), title, category, price);
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

            //Add texts to dialog if it's edit dialog
            if (mode == Mode.MODE_EDIT) {
                etTitle.setText(record.getTitle());
                etCategory.setText(record.getCategory());
                etPrice.setText(Integer.toString(record.getPrice()));
            }
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