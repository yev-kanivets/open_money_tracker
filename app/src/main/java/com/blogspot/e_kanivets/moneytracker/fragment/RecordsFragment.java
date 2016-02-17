package com.blogspot.e_kanivets.moneytracker.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
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
import com.blogspot.e_kanivets.moneytracker.activity.AddRecordActivity;
import com.blogspot.e_kanivets.moneytracker.activity.NavDrawerActivity;
import com.blogspot.e_kanivets.moneytracker.activity.ReportActivity;
import com.blogspot.e_kanivets.moneytracker.adapter.RecordAdapter;
import com.blogspot.e_kanivets.moneytracker.controller.AccountController;
import com.blogspot.e_kanivets.moneytracker.controller.CategoryController;
import com.blogspot.e_kanivets.moneytracker.controller.PeriodController;
import com.blogspot.e_kanivets.moneytracker.controller.RecordController;
import com.blogspot.e_kanivets.moneytracker.DbHelper;
import com.blogspot.e_kanivets.moneytracker.model.Category;
import com.blogspot.e_kanivets.moneytracker.model.Period;
import com.blogspot.e_kanivets.moneytracker.model.Record;
import com.blogspot.e_kanivets.moneytracker.repo.AccountRepo;
import com.blogspot.e_kanivets.moneytracker.repo.CategoryRepo;
import com.blogspot.e_kanivets.moneytracker.repo.IRepo;
import com.blogspot.e_kanivets.moneytracker.repo.RecordRepo;
import com.blogspot.e_kanivets.moneytracker.ui.AppRateDialog;
import com.blogspot.e_kanivets.moneytracker.ui.ChangeDateDialog;
import com.blogspot.e_kanivets.moneytracker.util.PrefUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecordsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecordsFragment extends Fragment {
    public static final String TAG = "RecordsFragment";

    private static final int REQUEST_ACTION_RECORD = 1;

    private List<Record> recordList;

    private RecordController recordController;
    private PeriodController periodController;

    @Bind(R.id.list_view)
    ListView listView;
    @Bind(R.id.tv_from_date)
    TextView tvFromDate;
    @Bind(R.id.tv_to_date)
    TextView tvToDate;

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        periodController = new PeriodController();

        DbHelper dbHelper = new DbHelper(getActivity());
        IRepo<Category> categoryRepo = new CategoryRepo(dbHelper);
        CategoryController categoryController = new CategoryController(categoryRepo);
        AccountController accountController = new AccountController(new AccountRepo(dbHelper));
        IRepo<Record> recordRepo = new RecordRepo(dbHelper);

        recordController = new RecordController(recordRepo, categoryController, accountController);
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
                Record record = recordList.get(info.position);
                if (record.isIncome())
                    startAddIncomeActivity(record, AddRecordActivity.Mode.MODE_EDIT);
                else startAddExpenseActivity(record, AddRecordActivity.Mode.MODE_EDIT);
                return true;
            case R.id.delete:
                recordController.delete(recordList.get(info.position));
                update();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @OnClick(R.id.btn_add_expense)
    public void addExpense() {
        startAddExpenseActivity(null, AddRecordActivity.Mode.MODE_ADD);
    }

    @OnClick(R.id.btn_add_income)
    public void addIncome() {
        startAddIncomeActivity(null, AddRecordActivity.Mode.MODE_ADD);
    }

    @OnClick(R.id.btn_report)
    public void showReport() {
        Intent intent = new Intent(getActivity(), ReportActivity.class);
        intent.putExtra(ReportActivity.KEY_PERIOD, periodController.getPeriod());
        startActivity(intent);
    }

    @OnClick(R.id.tv_from_date)
    public void showChangeFromDateDialog() {
        ChangeDateDialog dialog = new ChangeDateDialog(getActivity(),
                periodController.getPeriod().getFirst(), new ChangeDateDialog.OnDateChangedListener() {
            @Override
            public void OnDataChanged(Date date) {
                periodController.getPeriod().setFirst(date);
                update();

                tvFromDate.setText(periodController.getFirstDay());
            }
        });
        dialog.show();
    }

    @OnClick(R.id.tv_to_date)
    public void showChangeToDateDialog() {
        ChangeDateDialog dialog = new ChangeDateDialog(getActivity(),
                periodController.getPeriod().getLast(), new ChangeDateDialog.OnDateChangedListener() {
            @Override
            public void OnDataChanged(Date date) {
                periodController.getPeriod().setLast(date);
                update();

                tvToDate.setText(periodController.getLastDay());
            }
        });
        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == AppCompatActivity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_ACTION_RECORD:
                    update();
                    break;

                default:
                    break;
            }
        }
    }

    private void update() {
        recordList = recordController.getRecordsForPeriod(periodController.getPeriod());
        listView.setAdapter(new RecordAdapter(getActivity(), recordList));
        ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
    }

    private void initViews(View rootView) {
        if (rootView != null) {
            ButterKnife.bind(this, rootView);

            //Set dates of current week
            tvFromDate.setText(periodController.getFirstDay());
            tvToDate.setText(periodController.getLastDay());

            recordList = recordController.getRecordsForPeriod(periodController.getPeriod());

            listView.setAdapter(new RecordAdapter(getActivity(), recordList));
            ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();

            /* Scroll list to bottom only once at start */
            listView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                private boolean isFirst = true;

                @Override
                public void onGlobalLayout() {
                    if (isFirst) {
                        isFirst = false;
                        listView.setSelection(listView.getCount() - 1);
                        if (PrefUtils.checkRateDialog()) showAppRateDialog();
                    }
                }
            });

            registerForContextMenu(listView);

            ((NavDrawerActivity) getActivity()).onSectionAttached(TAG);
        }
    }

    private void initActionBar() {
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        ActionBar.LayoutParams lp = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT,
                Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        View customNav = LayoutInflater.from(getActivity()).inflate(R.layout.view_action_bar, null);

        Spinner spinner = (Spinner) customNav.findViewById(R.id.spinner_period);
        spinner.setAdapter(new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.array_periods)));
        spinner.setSelection(PrefUtils.readUsedPeriod());
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Calendar calendar = Calendar.getInstance();
                calendar.clear(Calendar.MINUTE);
                calendar.clear(Calendar.SECOND);
                calendar.clear(Calendar.MILLISECOND);

                switch (position) {
                    case 0:
                        periodController.setPeriod(Period.dayPeriod());
                        break;

                    case 1:
                        periodController.setPeriod(Period.weekPeriod());
                        break;

                    case 2:
                        periodController.setPeriod(Period.monthPeriod());
                        break;

                    case 3:
                        periodController.setPeriod(Period.yearPeriod());
                        break;

                    default:
                        break;
                }

                PrefUtils.writeUsedPeriod(position);

                update();

                tvFromDate.setText(periodController.getFirstDay());
                tvToDate.setText(periodController.getLastDay());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (actionBar != null) actionBar.setCustomView(customNav, lp);
    }

    private void showAppRateDialog() {
        AppRateDialog dialog = new AppRateDialog(getActivity());
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void startAddIncomeActivity(Record record, AddRecordActivity.Mode mode) {
        Intent intent = new Intent(getActivity(), AddRecordActivity.class);
        intent.putExtra(AddRecordActivity.KEY_RECORD, record);
        intent.putExtra(AddRecordActivity.KEY_MODE, mode);
        intent.putExtra(AddRecordActivity.KEY_TYPE, Record.TYPE_INCOME);
        startActivityForResult(intent, REQUEST_ACTION_RECORD);
    }

    private void startAddExpenseActivity(Record record, AddRecordActivity.Mode mode) {
        Intent intent = new Intent(getActivity(), AddRecordActivity.class);
        intent.putExtra(AddRecordActivity.KEY_RECORD, record);
        intent.putExtra(AddRecordActivity.KEY_MODE, mode);
        intent.putExtra(AddRecordActivity.KEY_TYPE, Record.TYPE_EXPENSE);
        startActivityForResult(intent, REQUEST_ACTION_RECORD);
    }
}