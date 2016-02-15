package com.blogspot.e_kanivets.moneytracker.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.activity.NavDrawerActivity;
import com.blogspot.e_kanivets.moneytracker.controller.RecordController;
import com.blogspot.e_kanivets.moneytracker.DbHelper;
import com.blogspot.e_kanivets.moneytracker.util.Constants;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ExportFragment extends Fragment {
    public static final String TAG = "ExportFragment";

    public static ExportFragment newInstance() {
        ExportFragment fragment = new ExportFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public ExportFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_export, container, false);
        initViews(rootView);
        initActionBar();
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        ((NavDrawerActivity) activity).onSectionAttached(TAG);
    }

    private void initViews(View rootView) {
        if (rootView != null) ButterKnife.bind(this, rootView);
    }

    private void initActionBar() {
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setCustomView(null);
        }
    }

    @OnClick(R.id.btn_export)
    public void exportRecords() {
        RecordController recordController = new RecordController(new DbHelper(getActivity()));
        List<String> records = recordController.getRecordsForExport(0, Long.MAX_VALUE);

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