package com.blogspot.e_kanivets.moneytracker.fragment;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.blogspot.e_kanivets.moneytracker.R;

/**
 * Base (abstract) class to encapsulate some common fragment operations.
 * Created on 3/12/16.
 *
 * @author Evgenii Kanivets
 */
public abstract class BaseFragment extends Fragment {
    @SuppressWarnings("unused")
    private static final String TAG = "BaseFragment";

    protected void initToolbar(View rootView) {
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
    }
}