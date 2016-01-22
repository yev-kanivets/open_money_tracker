package com.blogspot.e_kanivets.moneytracker.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.activity.NavDrawerActivity;
import com.blogspot.e_kanivets.moneytracker.helper.MTHelper;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddAccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddAccountFragment extends Fragment {
    public static final String TAG = "AddAccountFragment";

    private EditText etTitle;
    private EditText etInitSum;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AddAccountFragment.
     */
    public static AddAccountFragment newInstance() {
        AddAccountFragment fragment = new AddAccountFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public AddAccountFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_add_account, container, false);
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
                int initSum = Integer.parseInt(etInitSum.getText().toString());

                MTHelper.getInstance().addAccount(title, initSum);

                finish();
                return true;

            case R.id.action_close:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initViews(View rootView) {
        if (rootView != null) {
            etTitle = (EditText) rootView.findViewById(R.id.et_title);
            etInitSum = (EditText) rootView.findViewById(R.id.et_init_sum);
        }
    }

    private void initActionBar() {
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setCustomView(null);
        }
    }

    private void finish() {
        getActivity().getSupportFragmentManager().popBackStack();
    }
}