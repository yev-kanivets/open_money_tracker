package com.blogspot.e_kanivets.moneytracker.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.activity.NavDrawerActivity;
import com.blogspot.e_kanivets.moneytracker.helper.MTHelper;
import com.blogspot.e_kanivets.moneytracker.model.Record;
import com.blogspot.e_kanivets.moneytracker.util.AppUtils;

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
        View rootView = inflater.inflate(R.layout.fragment_add_income, container, false);
        initViews(rootView);
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        ((NavDrawerActivity) activity).onSectionAttached(TAG);
    }

    private void initViews(final View rootView) {
        if (rootView != null) {
            TextView tvTitle = (TextView) rootView.findViewById(R.id.tv_title);
            tvTitle.setText(R.string.income);
            tvTitle.setBackgroundColor(getResources().getColor(R.color.green_light));

            Button buttonAdd = (Button) rootView.findViewById(R.id.b_add);
            buttonAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String title = ((EditText) rootView.findViewById(R.id.et_title)).getText().toString();
                    String category = ((EditText) rootView.findViewById(R.id.et_category)).getText().toString();

                    //Check if price is valid
                    int price = 0;
                    try {
                        price = Integer.parseInt(((EditText) rootView.findViewById(R.id.et_price)).getText().toString());
                        if(price >= 0 && price <= 1000000000) {
                            if(mode == Mode.MODE_ADD) {
                                MTHelper.getInstance().addRecord(new Date().getTime(), 0, title, category, price);
                            }
                            if(mode == Mode.MODE_EDIT) {
                                MTHelper.getInstance().updateRecordById(record.getId(), title, category, price);
                            }
                            finish();
                        } else {
                            throw new NumberFormatException();
                        }
                    } catch (NumberFormatException e) {
                        Toast.makeText(getActivity(), getResources().getString(R.string.wrong_number_text),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });

            Button buttonCancel = (Button) rootView.findViewById(R.id.b_cancel);
            buttonCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            //Add texts to dialog if it's edit dialog
            if(mode == Mode.MODE_EDIT) {
                ((EditText) rootView.findViewById(R.id.et_title)).setText(record.getTitle());
                ((EditText) rootView.findViewById(R.id.et_category)).setText(record.getCategory());
                ((EditText) rootView.findViewById(R.id.et_price)).setText(Integer.toString(record.getPrice()));

                buttonAdd.setText(getResources().getString(R.string.save));
            }
        }
    }

    private void finish() {
        getActivity().getSupportFragmentManager().popBackStack();
    }

    public enum Mode {MODE_ADD, MODE_EDIT}
}