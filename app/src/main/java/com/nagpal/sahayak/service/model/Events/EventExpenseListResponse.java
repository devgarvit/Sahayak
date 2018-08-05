package com.nagpal.sahayak.service.model.Events;

import com.nagpal.sahayak.service.model.Entities.ExpenseList;

public class EventExpenseListResponse {
    ExpenseList expenseList;

    public EventExpenseListResponse(ExpenseList expenseList) {
        this.expenseList = expenseList;
    }

    public ExpenseList getInfo() {
        return expenseList;
    }
}
