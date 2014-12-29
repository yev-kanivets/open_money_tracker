package com.blogspot.e_kanivets.moneytracker.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.SimpleExpandableListAdapter;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.adapter.ReportItemAdapter;
import com.blogspot.e_kanivets.moneytracker.helper.MTHelper;
import com.blogspot.e_kanivets.moneytracker.model.Record;
import com.blogspot.e_kanivets.moneytracker.model.Report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by eugene on 11/09/14.
 */
public class ReportActivity extends Activity {

    private static final String TAG = ReportActivity.class.getSimpleName();
    private static final String TITLE_PARAM_NAME = "title";

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
            m.put(TITLE_PARAM_NAME, item.first);

            groupData.add(m);
        }

        /* List of attributes of groups for reading  */
        String groupFrom[] = new String[]{TITLE_PARAM_NAME};
        /* List of view IDs for information insertion */
        int groupTo[] = new int[]{android.R.id.text1};

        /* Create list for childDataItems */
        childData = new ArrayList<List<Map<String, String>>>();

        for (Map<String, String> group : groupData) {
            childDataItem = new ArrayList<Map<String, String>>();
            /* Fill up attribute names for each child item */
            for (Record record : MTHelper.getInstance().getRecords()) {
                if (record.getCategory().equals(group.get(TITLE_PARAM_NAME))) {
                    Log.d(TAG, record.getCategory());
                    m = new HashMap<String, String>();
                    m.put(TITLE_PARAM_NAME, record.getTitle());
                    childDataItem.add(m);
                }
            }

            /* Add childDataItem to common childData */
            childData.add(childDataItem);
        }

        /* List of attributes of childItems for reading  */
        String childFrom[] = new String[]{TITLE_PARAM_NAME};
        /* List of view IDs for information insertion */
        int childTo[] = new int[]{android.R.id.text1};

        expandableListView.setAdapter(new SimpleExpandableListAdapter(
                activity,
                groupData,
                android.R.layout.simple_expandable_list_item_1,
                groupFrom,
                groupTo,
                childData,
                android.R.layout.simple_list_item_1,
                childFrom,
                childTo));
    }
}
