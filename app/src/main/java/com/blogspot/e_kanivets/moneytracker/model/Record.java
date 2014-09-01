package com.blogspot.e_kanivets.moneytracker.model;

import com.blogspot.e_kanivets.moneytracker.helper.MTHelper;

/**
 * Created by eugene on 01/09/14.
 */
public class Record {

    private int id;
    private int time;
    private int type;
    private String title;
    private int categoryId;
    private String category;
    private int price;

    public Record(int id, int time, int type, String title, int categoryId, int price) {
        this.id = id;
        this.time = time;
        this.type = type;
        this.title = title;
        this.categoryId = categoryId;
        this.price = price;

        category = MTHelper.getInstance().getCategoryById(categoryId);
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

    public int getPrice() {
        return price;
    }
}
