package com.blogspot.e_kanivets.moneytracker.activity;

import com.blogspot.e_kanivets.moneytracker.activity.base.AddRecordBaseActivity;
import com.blogspot.e_kanivets.moneytracker.model.Account;
import com.blogspot.e_kanivets.moneytracker.model.Record;

import java.util.Date;

public class AddExpenseActivity extends AddRecordBaseActivity {
    @SuppressWarnings("unused")
    private static final String TAG = "AddExpenseActivity";

    @Override
    protected boolean doRecord(String title, String category, int price, Account account) {
        if (mode == Mode.MODE_ADD) recordController.addRecord(new Record(new Date().getTime(),
                Record.TYPE_EXPENSE, title, category, price, account.getId()));
        else if (mode == Mode.MODE_EDIT)
            recordController.updateRecordById(record.getId(), title, category, price, account.getId());

        return true;
    }
}