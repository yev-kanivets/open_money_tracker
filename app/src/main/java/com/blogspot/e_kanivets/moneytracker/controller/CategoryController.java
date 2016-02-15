package com.blogspot.e_kanivets.moneytracker.controller;

import com.blogspot.e_kanivets.moneytracker.DbHelper;
import com.blogspot.e_kanivets.moneytracker.model.Category;
import com.blogspot.e_kanivets.moneytracker.repo.IRepo;

import java.util.List;

/**
 * Controller class to encapsulate category handling logic.
 * Created on 1/23/16.
 *
 * @author Evgenii Kanivets
 */
public class CategoryController {
    @SuppressWarnings("unused")
    private static final String TAG = "CategoryController";

    private IRepo<Category> categoryRepo;

    public CategoryController(IRepo<Category> categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

    public Category readOrCreate(String categoryName) {
        String condition = DbHelper.NAME_COLUMN + "=?";
        String[] args = {categoryName};
        List<Category> categoryList = categoryRepo.readWithCondition(condition, args);

        if (categoryList.size() >= 1) return categoryList.get(0);
        else return categoryRepo.create(new Category(categoryName));
    }
}