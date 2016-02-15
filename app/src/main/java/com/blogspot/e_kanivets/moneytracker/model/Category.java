package com.blogspot.e_kanivets.moneytracker.model;

/**
 * Entity class for account.
 * Created on 01/09/14.
 *
 * @author Evgenii Kanivets
 */
public class Category {

    private int id;
    private String name;

    public Category(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Category(String name) {
        this.id = -1;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @SuppressWarnings("StringBufferReplaceableByString")
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Category {");
        sb.append("id = ").append(id);
        sb.append("}");

        return sb.toString();
    }
}