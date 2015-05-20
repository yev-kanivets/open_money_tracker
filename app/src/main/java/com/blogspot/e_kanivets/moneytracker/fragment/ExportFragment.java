package com.blogspot.e_kanivets.moneytracker.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
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
import com.blogspot.e_kanivets.moneytracker.helper.MTHelper;
import com.blogspot.e_kanivets.moneytracker.util.Constants;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExportFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExportFragment extends Fragment implements View.OnClickListener {
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
        View rootView = inflater.inflate(R.layout.fragment_export, container, false);
        initViews(rootView);
        initActionBar();
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        ((NavDrawerActivity) activity).onSectionAttached(position);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_export:
                exportRecords();
                break;

            default:
                break;
        }
    }

    private void initViews(View rootView) {
        if (rootView != null) {
            rootView.findViewById(R.id.btn_export).setOnClickListener(this);
        }
    }

    private void initActionBar() {
        ActionBar actionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();
        actionBar.setCustomView(null);
    }

    private void exportRecords() {
        List<String> records = MTHelper.getInstance().getRecordsForExport(0, Long.MAX_VALUE);

        File outFile = new File(Environment.getExternalStorageDirectory(), Constants.DEFAULT_EXPORT_FILE_NAME);
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(outFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (pw != null) {
            for (String record : records) {
                pw.println(record);
                pw.flush();
            }

            pw.flush();
            pw.close();
        }
    }
}