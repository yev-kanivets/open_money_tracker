package com.blogspot.e_kanivets.moneytracker.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import com.blogspot.e_kanivets.moneytracker.util.CategoryAutoCompleter;

import java.util.ArrayList;
import java.util.List;

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
}
