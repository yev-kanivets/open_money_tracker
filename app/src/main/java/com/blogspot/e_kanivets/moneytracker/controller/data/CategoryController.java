package com.blogspot.e_kanivets.moneytracker.controller.data;

import com.blogspot.e_kanivets.moneytracker.repo.DbHelper;
import com.blogspot.e_kanivets.moneytracker.controller.base.BaseController;
import com.blogspot.e_kanivets.moneytracker.entity.data.Category;
import com.blogspot.e_kanivets.moneytracker.repo.base.IRepo;

import java.util.List;

/**
 * Controller class to encapsulate category handling logic.
 * Created on 1/23/16.
 *
 * @author Evgenii Kanivets
 */
public class CategoryController extends BaseController<Category> {
    @SuppressWarnings("unused")
    private static final String TAG = "CategoryController";

    public CategoryController(IRepo<Category> categoryRepo) {
        super(categoryRepo);
    }

    public Category readOrCreate(String categoryName) {
        String condition = DbHelper.NAME_COLUMN + "=?";
        String[] args = {categoryName};
        List<Category> categoryList = repo.readWithCondition(condition, args);

        if (categoryList.size() >= 1) return categoryList.get(0);
        else return repo.create(new Category(categoryName));
    }
}