package com.nagpal.sahayak.service.model.Entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExpenseDetailsResponse {

        @SerializedName("data")
        @Expose
        private ExpenseData data;

        public ExpenseData getData() {
            return data;
        }

        public void setData(ExpenseData data) {
            this.data = data;
        }

}
