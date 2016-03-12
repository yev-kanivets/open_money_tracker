package com.blogspot.e_kanivets.moneytracker.fragment;

import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

/**
 * Base (abstract) class to encapsulate some common fragment operations.
 * Created on 3/12/16.
 *
 * @author Evgenii Kanivets
 */
public abstract class BaseFragment extends Fragment {
    @SuppressWarnings("unused")
    private static final String TAG = "BaseFragment";

    protected ActionBar getToolbar() {
        return ((AppCompatActivity) getActivity()).getSupportActionBar();
    }
}