package com.nagpal.sahayak.service.model.Entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExpenseCategoryResponse {

    @SerializedName("data")
    @Expose
    private CategoryData data;

    public CategoryData getData() {
        return data;
    }

    public void setData(CategoryData data) {
        this.data = data;
    }

}
