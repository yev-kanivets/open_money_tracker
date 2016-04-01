package com.blogspot.e_kanivets.moneytracker.activity.exchange_rate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.blogspot.e_kanivets.moneytracker.MtApp;
import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.activity.base.BaseBackActivity;
import com.blogspot.e_kanivets.moneytracker.adapter.ExchangeRateAdapter;
import com.blogspot.e_kanivets.moneytracker.controller.ExchangeRateController;
import com.blogspot.e_kanivets.moneytracker.entity.ExchangeRate;
import com.blogspot.e_kanivets.moneytracker.util.ExchangeRatesSummarizer;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

public class ExchangeRatesActivity extends BaseBackActivity {
    @SuppressWarnings("unused")
    private static final String TAG = "ExchangeRatesActivity";

    private static final int REQUEST_ADD_EXCHANGE_RATE = 1;

    @Inject
    ExchangeRateController rateController;

    private List<ExchangeRate> exchangeRateList;

    @Bind(R.id.list_view)
    ListView listView;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_exchange_rates;
    }

    @Override
    protected boolean initData() {
        boolean result = super.initData();
        MtApp.get().getAppComponent().inject(ExchangeRatesActivity.this);
        return result;
    }

    @Override
    protected void initViews() {
        super.initViews();

        registerForContextMenu(listView);
        update();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu_exchange_rate, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
            case R.id.delete:
                rateController.delete(exchangeRateList.get(info.position));
                update();
                setResult(RESULT_OK);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @OnClick(R.id.btn_add_exchange_rate)
    public void addExchangeRate() {
        Intent intent = new Intent(ExchangeRatesActivity.this, AddExchangeRateActivity.class);
        startActivityForResult(intent, REQUEST_ADD_EXCHANGE_RATE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == AppCompatActivity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_ADD_EXCHANGE_RATE:
                    update();
                    setResult(RESULT_OK);
                    break;

                default:
                    break;
            }
        }
    }

    private void update() {
        exchangeRateList = new ExchangeRatesSummarizer(rateController.readAll()).getSummaryList();
        Collections.reverse(exchangeRateList);

        listView.setAdapter(new ExchangeRateAdapter(ExchangeRatesActivity.this, exchangeRateList));
        ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
    }
}
