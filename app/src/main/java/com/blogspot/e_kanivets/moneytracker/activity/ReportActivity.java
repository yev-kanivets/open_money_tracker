package com.blogspot.e_kanivets.moneytracker.activity;

import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.activity.base.BaseActivity;
import com.blogspot.e_kanivets.moneytracker.adapter.ExpandableListReportAdapter;
import com.blogspot.e_kanivets.moneytracker.adapter.ReportItemAdapter;
import com.blogspot.e_kanivets.moneytracker.controller.AccountController;
import com.blogspot.e_kanivets.moneytracker.controller.CategoryController;
import com.blogspot.e_kanivets.moneytracker.controller.RecordController;
import com.blogspot.e_kanivets.moneytracker.DbHelper;
import com.blogspot.e_kanivets.moneytracker.model.Category;
import com.blogspot.e_kanivets.moneytracker.model.Period;
import com.blogspot.e_kanivets.moneytracker.model.Record;
import com.blogspot.e_kanivets.moneytracker.model.Report;
import com.blogspot.e_kanivets.moneytracker.repo.AccountRepo;
import com.blogspot.e_kanivets.moneytracker.repo.CategoryRepo;
import com.blogspot.e_kanivets.moneytracker.repo.IRepo;
import com.blogspot.e_kanivets.moneytracker.repo.RecordRepo;
import com.blogspot.e_kanivets.moneytracker.util.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

public class ReportActivity extends BaseActivity {
    @SuppressWarnings("unused")
    private static final String TAG = "ReportActivity";

    public static final String KEY_PERIOD = "key_period";

    private Report report;

    @Bind(R.id.list_view)
    ListView listView;
    @Bind(R.id.exp_list_view)
    ExpandableListView expandableListView;

    private RecordController recordController;
    private Period period;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_report;
    }

    @Override
    protected boolean initData() {
        super.initData();

        DbHelper dbHelper = new DbHelper(ReportActivity.this);
        IRepo<Category> categoryRepo = new CategoryRepo(dbHelper);
        CategoryController categoryController = new CategoryController(categoryRepo);
        AccountController accountController = new AccountController(new AccountRepo(dbHelper));

        recordController = new RecordController(new RecordRepo(dbHelper), categoryController,
                accountController);

        period = getIntent().getParcelableExtra(KEY_PERIOD);
        report = new Report(recordController.getRecordsForPeriod(period));

        return period != null;
    }

    @Override
    protected void initViews() {
        super.initViews();

        listView.setAdapter(new ReportItemAdapter(ReportActivity.this,
                new Report(recordController.getRecordsForPeriod(period)).getReportList()));

        /* Scroll list to bottom only once at start */
        listView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            private boolean isFirst = true;

            @Override
            public void onGlobalLayout() {
                if (isFirst) {
                    isFirst = false;
                    listView.setSelection(listView.getCount() - 1);
                }
            }
        });

        initExpandableListView();
    }

    private void initExpandableListView() {
        /* List for groups */
        List<Map<String, String>> groupData;

        /* List for child items */
        List<Map<String, String>> childDataItem;

        /* List for childDataItems */
        List<List<Map<String, String>>> childData;

        /* Buffer map for names of values */
        Map<String, String> m;

        /* Fill the group list */
        groupData = new ArrayList<>();
        for (Pair<String, Integer> item : report.getReportList()) {
            /* Fill up attribute names for each group */
            m = new HashMap<>();
            m.put(Constants.TITLE_PARAM_NAME, item.first);
            m.put(Constants.PRICE_PARAM_NAME, Integer.toString(item.second));

            groupData.add(m);
        }

        /* List of attributes of groups for reading  */
        String groupFrom[] = new String[]{Constants.TITLE_PARAM_NAME, Constants.PRICE_PARAM_NAME};
        /* List of view IDs for information insertion */
        int groupTo[] = new int[]{R.id.tv_category, R.id.tv_total};

        /* Create list for childDataItems */
        childData = new ArrayList<>();

        for (Map<String, String> group : groupData) {
            childDataItem = new ArrayList<>();
            /* Fill up attribute names for each child item */
            for (Record record : report.getSummaryRecordList()) {
                if (record.getCategory().equals(group.get(Constants.TITLE_PARAM_NAME))) {
                    int price = record.getPrice();
                    if (!record.isIncome()) {
                        price *= -1;
                    }

                    m = new HashMap<>();
                    m.put(Constants.TITLE_PARAM_NAME, record.getTitle());
                    m.put(Constants.PRICE_PARAM_NAME, Integer.toString(price));

                    childDataItem.add(m);
                }
            }

            /* Add childDataItem to common childData */
            childData.add(childDataItem);
        }

        /* List of attributes of childItems for reading  */
        String childFrom[] = new String[]{Constants.TITLE_PARAM_NAME, Constants.PRICE_PARAM_NAME};
        /* List of view IDs for information insertion */
        int childTo[] = new int[]{R.id.tv_category, R.id.tv_total};

        expandableListView.addFooterView(getSummaryReportView(report.getSummaryReportList()));
        expandableListView.setAdapter(new ExpandableListReportAdapter(
                ReportActivity.this,
                groupData,
                R.layout.view_report_item_exp,
                groupFrom,
                groupTo,
                childData,
                R.layout.view_report_item,
                childFrom,
                childTo) {
        });
    }

    private View getSummaryReportView(List<Pair<String, Integer>> summaryReportList) {
        ViewGroup viewGroup = (ViewGroup) getLayoutInflater().inflate(R.layout.view_summary_report, null);

        ReportItemAdapter adapter = new ReportItemAdapter(ReportActivity.this, summaryReportList);

        for (int i = 0; i < adapter.getCount(); i++) {
            viewGroup.addView(adapter.getView(i, null, null));
        }

        return viewGroup;
    }
}