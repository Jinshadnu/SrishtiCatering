package com.vingcoz.srishticatering.models.api.categ;

import com.google.gson.annotations.SerializedName;

public class CategoryDataItem {

    @SerializedName("id")
    private int id;

    @SerializedName("Name")
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return
                "CategoryDataItem{" +
                        "id = '" + id + '\'' +
                        ",name = '" + name + '\'' +
                        "}";
    }
}