package com.blogspot.e_kanivets.moneytracker.model;

import java.util.Calendar;

/**
 * Created by eugene on 01/09/14.
 */
public class Category {

    private int id;
    private String name;

    public Category(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
