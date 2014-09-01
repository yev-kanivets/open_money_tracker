package com.blogspot.e_kanivets.moneytracker.model;

/**
 * Created by eugene on 01/09/14.
 */
public class Record {

    private String title;
    private String category;
    private String price;

    public Record(String title, String category, String price) {
        this.title = title;
        this.category = category;
        this.price = price;
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
