package com.blogspot.e_kanivets.moneytracker.model;

import com.blogspot.e_kanivets.moneytracker.controller.CategoryController;
import com.blogspot.e_kanivets.moneytracker.helper.DbHelper;
import com.blogspot.e_kanivets.moneytracker.MtApp;

import java.io.Serializable;

/**
 * Entity class.
 * Created by eugene on 01/09/14.
 */
public class Record implements Serializable {
    public static final int TYPE_INCOME = 0;
    public static final int TYPE_EXPENSE = 1;

    private int id;
    private long time;
    private int type;
    private String title;
    private int categoryId;
    private String category;
    private int price;
    private int accountId;

    public Record(int id, long time, int type, String title, int categoryId, int price, int accountId) {
        this.id = id;
        this.time = time;
        this.type = type;
        this.title = title;
        this.categoryId = categoryId;
        this.price = price;
        this.accountId = accountId;

        category = new CategoryController(new DbHelper(MtApp.get())).getCategoryById(categoryId);
    }

    public Record(long time, int type, String title, String category, int price, int accountId) {
        this.time = time;
        this.type = type;
        this.title = title;
        this.category = category;
        this.price = price;
        this.accountId = accountId;
    }

    public int getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public int getPrice() {
        return price;
    }

    public long getTime() {
        return time;
    }

    public boolean isIncome() {
        return type == 0;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }
}