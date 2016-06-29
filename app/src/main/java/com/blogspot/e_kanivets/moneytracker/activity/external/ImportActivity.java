package com.blogspot.e_kanivets.moneytracker.activity.external;

import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.activity.base.BaseBackActivity;
import com.blogspot.e_kanivets.moneytracker.controller.external.ImportController;
import com.blogspot.e_kanivets.moneytracker.entity.data.Record;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

public class ImportActivity extends BaseBackActivity {
    @Inject
    ImportController importController;

    @Bind(R.id.et_import_data)
    EditText etImportData;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_import;
    }

    @Override
    protected boolean initData() {
        boolean result = super.initData();
        getAppComponent().inject(ImportActivity.this);
        return result;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_import, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_help:
                AlertDialog.Builder builder = new AlertDialog.Builder(ImportActivity.this);
                builder.setTitle(R.string.help)
                        .setMessage(R.string.import_help)
                        .setPositiveButton(android.R.string.ok, null)
                        .show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @OnClick(R.id.btn_import)
    public void importRecords() {
        final String data = etImportData.getText().toString().trim();

        AsyncTask<Void, Void, Integer> importTask = new AsyncTask<Void, Void, Integer>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                startProgress();
            }

            @Override
            protected Integer doInBackground(Void... params) {
                List<Record> recordList = importController.importRecordsFromCsv(data);
                return recordList.size();
            }

            @Override
            protected void onPostExecute(Integer recordCount) {
                super.onPostExecute(recordCount);
                stopProgress();
                showToast(getString(R.string.records_imported, recordCount));
                setResult(RESULT_OK);
                finish();
            }
        };
        importTask.execute();
    }
}
