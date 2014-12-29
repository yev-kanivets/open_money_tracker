package com.blogspot.e_kanivets.moneytracker.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.adapter.ExpandableListReportAdapter;
import com.blogspot.e_kanivets.moneytracker.adapter.ReportItemAdapter;
import com.blogspot.e_kanivets.moneytracker.helper.MTHelper;
import com.blogspot.e_kanivets.moneytracker.model.Record;
import com.blogspot.e_kanivets.moneytracker.model.Report;
import com.blogspot.e_kanivets.moneytracker.util.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by eugene on 11/09/14.
 */
public class ReportActivity extends Activity {

    private static final String TAG = ReportActivity.class.getSimpleName();

    private Activity activity;
    private Report report;

    private ListView listView;
    private ExpandableListView expandableListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_report);

        activity = this;
        report = new Report(MTHelper.getInstance().getRecords());

        initViews();
    }

    private void initViews() {
        listView = (ListView) findViewById(R.id.listView);
        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);

        /*tvTitle.setText("REPORT (" + MTHelper.getInstance().getFirstDay() + " - "
                + MTHelper.getInstance().getLastDay() + ")");*/

        findViewById(R.id.btn_thank_author).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReportActivity.this, ThankAuthorActivity.class);
                startActivity(intent);
            }
        });

        listView.setAdapter(new ReportItemAdapter(activity,
                new Report(MTHelper.getInstance().getRecords()).getReportList()));

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
        groupData = new ArrayList<Map<String, String>>();
        for (Pair<String, Integer> item : report.getReportList()) {
            /* Fill up attribute names for each group */
            m = new HashMap<String, String>();
            m.put(Constants.TITLE_PARAM_NAME, item.first);
            m.put(Constants.PRICE_PARAM_NAME, Integer.toString(item.second));

            groupData.add(m);
        }

        /* List of attributes of groups for reading  */
        String groupFrom[] = new String[]{Constants.TITLE_PARAM_NAME, Constants.PRICE_PARAM_NAME};
        /* List of view IDs for information insertion */
        int groupTo[] = new int[]{R.id.tv_category, R.id.tv_total};

        /* Create list for childDataItems */
        childData = new ArrayList<List<Map<String, String>>>();

        for (Map<String, String> group : groupData) {
            childDataItem = new ArrayList<Map<String, String>>();
            /* Fill up attribute names for each child item */
            for (Record record : MTHelper.getInstance().getRecords()) {
                if (record.getCategory().equals(group.get(Constants.TITLE_PARAM_NAME))) {
                    int price = record.getPrice();
                    if (!record.isIncome()) {
                        price *= -1;
                    }

                    m = new HashMap<String, String>();
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
                activity,
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

        ReportItemAdapter adapter = new ReportItemAdapter(activity, summaryReportList);

        for (int i = 0; i < adapter.getCount(); i++) {
            viewGroup.addView(adapter.getView(i, null, null));
        }

        return viewGroup;
    }
}