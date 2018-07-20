package com.nagpal.sahayak.service.model.Entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExpenseData {
    public class Data {

        @SerializedName("expense")
        @Expose
        private Expense expense;

        public Expense getExpense() {
            return expense;
        }

        public void setExpense(Expense expense) {
            this.expense = expense;
        }

    }
}
