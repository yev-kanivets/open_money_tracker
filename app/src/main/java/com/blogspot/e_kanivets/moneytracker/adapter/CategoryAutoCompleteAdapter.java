package com.blogspot.e_kanivets.moneytracker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.util.CategoryAutoCompleter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Custom adapter to autocomplete categories.
 * Created on 3/18/16.
 *
 * @author Evgenii Kanivets
 */
public class CategoryAutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {
    private final CategoryAutoCompleter autoCompleter;
    private List<String> resultList;

    public CategoryAutoCompleteAdapter(Context context, int resource, CategoryAutoCompleter autoCompleter) {
        super(context, resource);
        this.autoCompleter = autoCompleter;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.view_category_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else viewHolder = (ViewHolder) convertView.getTag();

        final String category = resultList.get(position);

        viewHolder.tvCategory.setText(category);
        viewHolder.ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoCompleter.removeFromAutoComplete(category);
                resultList.remove(category);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    @Override
    public int getCount() {
        return resultList.size();
    }

    @Override
    public String getItem(int index) {
        return resultList.get(index);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();

                List<String> tempList;

                if (constraint != null) tempList = autoCompleter.completeByPart(constraint.toString());
                else tempList = new ArrayList<>();

                filterResults.values = tempList;
                filterResults.count = tempList.size();

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null) //noinspection unchecked
                    resultList = (List<String>) results.values;

                if (results != null && results.count > 0) notifyDataSetChanged();
                else notifyDataSetInvalidated();
            }
        };
    }

    public static class ViewHolder {
        @BindView(R.id.tvCategory)
        TextView tvCategory;
        @BindView(R.id.iv_cancel)
        View ivCancel;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
