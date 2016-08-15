package com.blogspot.e_kanivets.moneytracker.util;

import com.blogspot.e_kanivets.moneytracker.controller.data.CategoryController;
import com.blogspot.e_kanivets.moneytracker.entity.data.Category;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Util class to encapsulate category autocomplete logic.
 * Created on 3/18/16.
 *
 * @author Evgenii Kanivets
 */
public class CategoryAutoCompleter {
    private List<String> categoryList;
    private Set<String> filterSet;

    public CategoryAutoCompleter(CategoryController categoryController) {
        categoryList = new ArrayList<>();
        filterSet = new HashSet<>();

        for (Category category : categoryController.readAll()) {
            categoryList.add(category.getName());
        }
    }

    public List<String> completeByPart(String part) {
        List<String> resultList = new ArrayList<>();

        for (String category : categoryList) {
            if (category.startsWith(part)) resultList.add(category);
        }

        return resultList;
    }

    public void removeFromAutoComplete(String category) {
        filterSet.add(category);
        categoryList.remove(category);
    }
}
