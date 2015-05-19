package com.blogspot.e_kanivets.moneytracker.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.activity.NavDrawerActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExportFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExportFragment extends Fragment {
    private static final String KEY_POSITION = "key_position";

    private int position;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ExportFragment.
     */
    public static ExportFragment newInstance(int position) {
        ExportFragment fragment = new ExportFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_POSITION, position);
        fragment.setArguments(args);
        fragment.position = position;
        return fragment;
    }

    public ExportFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            position = getArguments().getInt(KEY_POSITION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        initActionBar();
        return inflater.inflate(R.layout.fragment_export, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        ((NavDrawerActivity) activity).onSectionAttached(position);
    }

    private void initActionBar() {
        ActionBar actionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();
        actionBar.setCustomView(null);
    }
}