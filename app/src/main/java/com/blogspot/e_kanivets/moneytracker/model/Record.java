package com.blogspot.e_kanivets.moneytracker.model;

/**
 * Created by eugene on 01/09/14.
 */
public class Record {

    private int id;

    private String title;
    private String category;
    private String price;

    public Record(int id, String title, String category, String price) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.price = price;
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

    public String getPrice() {
        return price;
    }
}
