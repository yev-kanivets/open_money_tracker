package com.blogspot.e_kanivets.moneytracker.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.activity.NavDrawerActivity;
import com.blogspot.e_kanivets.moneytracker.activity.ReportActivity;
import com.blogspot.e_kanivets.moneytracker.adapter.RecordAdapter;
import com.blogspot.e_kanivets.moneytracker.helper.MTHelper;
import com.blogspot.e_kanivets.moneytracker.helper.PeriodHelper;
import com.blogspot.e_kanivets.moneytracker.model.Record;
import com.blogspot.e_kanivets.moneytracker.ui.AppRateDialog;
import com.blogspot.e_kanivets.moneytracker.ui.ChangeDateDialog;
import com.blogspot.e_kanivets.moneytracker.util.AppUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecordsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecordsFragment extends Fragment implements View.OnClickListener, Observer, AdapterView.OnItemSelectedListener {
    public static final String TAG = "RecordsFragment";

    private ListView listView;

    private TextView tvFromDate;
    private TextView tvToDate;

    private OnFragmentInteractionListener listener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RecordsFragment.
     */
    public static RecordsFragment newInstance() {
        RecordsFragment fragment = new RecordsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public RecordsFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_records, container, false);
        initViews(rootView);
        initActionBar();
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            listener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }

        ((NavDrawerActivity) activity).onSectionAttached(TAG);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        getActivity().getMenuInflater().inflate(R.menu.menu_record, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
            case R.id.edit:
                Record record = MTHelper.getInstance().getRecords().get(info.position);
                listener.onEditRecord(record);
                return true;
            case R.id.delete:
                MTHelper.getInstance().deleteRecordById(MTHelper.getInstance().getRecords().
                        get(info.position).getId());
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add_expense:
                //showAddExpenseDialog();
                listener.onAddExpensePressed();
                break;

            case R.id.btn_add_income:
                //showAddIncomeDialog();
                listener.onAddIncomePressed();
                break;

            case R.id.btn_report:
                Intent intent = new Intent(getActivity(), ReportActivity.class);
                startActivity(intent);
                break;

            case R.id.tv_from_date:
                showChangeFromDateDialog();
                break;

            case R.id.tv_to_date:
                showChangeToDateDialog();
                break;

            default:
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear(Calendar.MINUTE);
        calendar.clear(Calendar.SECOND);
        calendar.clear(Calendar.MILLISECOND);

        switch (position) {
            // Day period
            case 0:
                MTHelper.getInstance().setPeriod(PeriodHelper.getInstance().getDayPeriod());
                break;
            // Week period
            case 1:
                MTHelper.getInstance().setPeriod(PeriodHelper.getInstance().getWeekPeriod());
                break;
            // Month period
            case 2:
                MTHelper.getInstance().setPeriod(PeriodHelper.getInstance().getMonthPeriod());
                break;
            // Year period
            case 3:
                MTHelper.getInstance().setPeriod(PeriodHelper.getInstance().getYearPeriod());
                break;
            default:
                break;
        }

        AppUtils.writeUsedPeriod(getActivity(), position);

        MTHelper.getInstance().update();

        tvFromDate.setText(MTHelper.getInstance().getFirstDay());
        tvToDate.setText(MTHelper.getInstance().getLastDay());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void update(Observable observable, Object o) {
        ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
    }

    private void initViews(View rootView) {
        if (rootView != null) {
            View btnAddIncome = rootView.findViewById(R.id.btn_add_income);
            View btnAddExpense = rootView.findViewById(R.id.btn_add_expense);
            View btnReport = rootView.findViewById(R.id.btn_report);

            tvFromDate = (TextView) rootView.findViewById(R.id.tv_from_date);
            tvToDate = (TextView) rootView.findViewById(R.id.tv_to_date);

            listView = (ListView) rootView.findViewById(R.id.listView);

            //Set dates of current week
            tvFromDate.setText(MTHelper.getInstance().getFirstDay());
            tvToDate.setText(MTHelper.getInstance().getLastDay());

            //Set listeners
            btnAddIncome.setOnClickListener(RecordsFragment.this);
            btnAddExpense.setOnClickListener(RecordsFragment.this);
            tvFromDate.setOnClickListener(RecordsFragment.this);
            tvToDate.setOnClickListener(RecordsFragment.this);
            btnReport.setOnClickListener(RecordsFragment.this);

            listView.setAdapter(new RecordAdapter(getActivity(), MTHelper.getInstance().getRecords()));
            ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
            registerForContextMenu(listView);

            /* Scroll list to bottom only once at start */
            listView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                private boolean isFirst = true;

                @Override
                public void onGlobalLayout() {
                    if (isFirst) {
                        isFirst = false;
                        listView.setSelection(listView.getCount() - 1);
                        if (AppUtils.checkRateDialog(getActivity())) {
                            showAppRateDialog();
                        }
                    }
                }
            });

            //Subscribe to helper
            MTHelper.getInstance().addObserver(this);
        }
    }

    private void initActionBar() {
        ActionBar actionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();

        ActionBar.LayoutParams lp = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT,
                Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        View customNav = LayoutInflater.from(getActivity()).inflate(R.layout.view_action_bar, null);

        Spinner spinner = (Spinner) customNav.findViewById(R.id.spinner_period);
        spinner.setAdapter(new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.array_periods)));
        spinner.setSelection(AppUtils.readUsedPeriod(getActivity()));
        spinner.setOnItemSelectedListener(this);

        if (actionBar != null) {
            actionBar.setCustomView(customNav, lp);
        }
    }

    private void showChangeFromDateDialog() {
        ChangeDateDialog dialog = new ChangeDateDialog(getActivity(),
                MTHelper.getInstance().getPeriod().getFirst(), new ChangeDateDialog.OnDateChangedListener() {
            @Override
            public void OnDataChanged(Date date) {
                MTHelper.getInstance().getPeriod().setFirst(date);
                MTHelper.getInstance().update();

                tvFromDate.setText(MTHelper.getInstance().getFirstDay());
            }
        });
        dialog.show();
    }

    private void showChangeToDateDialog() {
        ChangeDateDialog dialog = new ChangeDateDialog(getActivity(),
                MTHelper.getInstance().getPeriod().getLast(), new ChangeDateDialog.OnDateChangedListener() {
            @Override
            public void OnDataChanged(Date date) {
                MTHelper.getInstance().getPeriod().setLast(date);
                MTHelper.getInstance().update();

                tvToDate.setText(MTHelper.getInstance().getLastDay());
            }
        });
        dialog.show();
    }

    private void showAppRateDialog() {
        AppRateDialog dialog = new AppRateDialog(getActivity());
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    public interface OnFragmentInteractionListener {
        void onAddIncomePressed();
        void onAddExpensePressed();
        void onEditRecord(Record record);
    }
}