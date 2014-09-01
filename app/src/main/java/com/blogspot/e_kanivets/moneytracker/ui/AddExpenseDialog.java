package com.blogspot.e_kanivets.moneytracker.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.helper.DBHelper;
import com.blogspot.e_kanivets.moneytracker.util.MTApp;

/**
 * Created by eugene on 29/08/14.
 */
public class AddExpenseDialog extends AlertDialog {

    private Context context;
    private DBHelper dbHelper;

    public AddExpenseDialog(Context context) {
        super(context);
        this.context = context;

        dbHelper = MTApp.get().getDbHelper();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View layout = getLayoutInflater().inflate(R.layout.dialog_add_record, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(layout);

        final AlertDialog dialog = builder.show();

        TextView tvTitle = (TextView) layout.findViewById(R.id.tv_title);
        tvTitle.setText(R.string.expense);
        tvTitle.setBackgroundColor(context.getResources().getColor(R.color.red_light));

        Button buttonAdd = (Button) layout.findViewById(R.id.b_add);
        buttonAdd.setText(context.getResources().getString(R.string.add_expense));
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Button buttonCancel = (Button) layout.findViewById(R.id.b_cancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }


}
