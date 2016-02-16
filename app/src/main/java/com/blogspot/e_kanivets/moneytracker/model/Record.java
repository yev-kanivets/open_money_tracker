package com.blogspot.e_kanivets.moneytracker.model;

import com.blogspot.e_kanivets.moneytracker.DbHelper;
import com.blogspot.e_kanivets.moneytracker.MtApp;
import com.blogspot.e_kanivets.moneytracker.repo.CategoryRepo;

import java.io.Serializable;

/**
 * Entity class.
 * Created by eugene on 01/09/14.
 */
public class Record implements IEntity, Serializable {
    public static final int TYPE_INCOME = 0;
    public static final int TYPE_EXPENSE = 1;

    private long id;
    private long time;
    private int type;
    private String title;
    private long categoryId;
    private String category;
    private int price;
    private long accountId;

    public Record(long id, long time, int type, String title, long categoryId, int price, long accountId) {
        this.id = id;
        this.time = time;
        this.type = type;
        this.title = title;
        this.categoryId = categoryId;
        this.price = price;
        this.accountId = accountId;

        // TODO: Refactor this shit.
        Category categoryActual = new CategoryRepo(new DbHelper(MtApp.get())).read(categoryId);
        if (categoryActual != null) category = categoryActual.getName();
    }

    public Record(long time, int type, String title, String category, int price, long accountId) {
        this.time = time;
        this.type = type;
        this.title = title;
        this.categoryId = -1;
        this.category = category;
        this.price = price;
        this.accountId = accountId;
    }

    public int getType() {
        return type;
    }

    @Override
    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public long getCategoryId() {
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

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }
}