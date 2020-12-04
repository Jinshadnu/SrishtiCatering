package com.vingcoz.srishticatering.models.api.categ;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoryResponse {

    @SerializedName("category")
    private List<CategoryDataItem> categoryData;

    @SerializedName("error")
    private boolean error;

    @SerializedName("message")
    private String message;

    public List<CategoryDataItem> getCategoryData() {
        return categoryData;
    }

    public void setCategoryData(List<CategoryDataItem> categoryData) {
        this.categoryData = categoryData;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return
                "CategoryResponse{" +
                        "categoryData = '" + categoryData + '\'' +
                        ",error = '" + error + '\'' +
                        ",message = '" + message + '\'' +
                        "}";
    }
}