package com.blogspot.e_kanivets.moneytracker.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.blogspot.e_kanivets.moneytracker.DbHelper;
import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.activity.AddExchangeRateActivity;
import com.blogspot.e_kanivets.moneytracker.adapter.ExchangeRateAdapter;
import com.blogspot.e_kanivets.moneytracker.controller.ExchangeRateController;
import com.blogspot.e_kanivets.moneytracker.entity.ExchangeRate;
import com.blogspot.e_kanivets.moneytracker.repo.ExchangeRateRepo;

import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ExchangeRatesFragment extends BaseFragment {
    public static final String TAG = "ExchangeRatesFragment";

    private static final int REQUEST_ADD_EXCHANGE_RATE = 1;

    @Bind(R.id.list_view)
    ListView listView;

    private ExchangeRateController rateController;
    private List<ExchangeRate> exchangeRateList;

    public static ExchangeRatesFragment newInstance() {
        ExchangeRatesFragment fragment = new ExchangeRatesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public ExchangeRatesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rateController = new ExchangeRateController(new ExchangeRateRepo(new DbHelper(getActivity())));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_exchange_rates, container, false);
        getActivity().setTitle(R.string.title_exchange_rates);
        initViews(rootView);
        return rootView;
    }

    @OnClick(R.id.btn_add_exchange_rate)
    public void addExchangeRate() {
        Intent intent = new Intent(getActivity(), AddExchangeRateActivity.class);
        startActivityForResult(intent, REQUEST_ADD_EXCHANGE_RATE);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.menu_exchange_rate, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
            case R.id.delete:
                rateController.delete(exchangeRateList.get(info.position));
                update();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == AppCompatActivity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_ADD_EXCHANGE_RATE:
                    update();
                    break;

                default:
                    break;
            }
        }
    }

    private void update() {
        exchangeRateList = rateController.readAll();
        Collections.reverse(exchangeRateList);

        listView.setAdapter(new ExchangeRateAdapter(getActivity(), exchangeRateList));
        ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
    }

    private void initViews(View rootView) {
        if (rootView == null) return;

        ButterKnife.bind(this, rootView);

        registerForContextMenu(listView);
        update();
    }
}
