package com.blogspot.e_kanivets.moneytracker.model;

/**
 * Entity class for account.
 * Created on 01/09/14.
 *
 * @author Evgenii Kanivets
 */
public class Category implements IEntity {

    private long id;
    private String name;

    public Category(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Category(String name) {
        this.id = -1;
        this.name = name;
    }

    @Override
    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Category) {
            Category category = (Category) o;
            return this.id == category.getId()
                    && this.name.equals(category.getName());
        } else return false;
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