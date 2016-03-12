package com.blogspot.e_kanivets.moneytracker.fragment;

import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    protected abstract void initViews(View rootView);

    protected void inflateAppBarLayout(@LayoutRes int layoutResId) {
        ViewGroup container = (ViewGroup) getActivity().findViewById(R.id.app_bar_container);
        if (container == null) return;

        container.removeAllViews();

        if (layoutResId == -1) return;

        LayoutInflater.from(getActivity()).inflate(layoutResId, container, true);
    }
}