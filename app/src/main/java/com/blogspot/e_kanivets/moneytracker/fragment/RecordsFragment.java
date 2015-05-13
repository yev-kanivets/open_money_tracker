package com.blogspot.e_kanivets.moneytracker.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.activity.NavDrawerActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecordsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecordsFragment extends Fragment {
    private static final String KEY_POSITION = "key_position";

    private int position;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RecordsFragment.
     */
    public static RecordsFragment newInstance(int position) {
        RecordsFragment fragment = new RecordsFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_POSITION, position);
        fragment.setArguments(args);
        fragment.position = position;
        return fragment;
    }

    public RecordsFragment() {
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
        return inflater.inflate(R.layout.fragment_records, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        ((NavDrawerActivity) activity).onSectionAttached(position);
    }
}