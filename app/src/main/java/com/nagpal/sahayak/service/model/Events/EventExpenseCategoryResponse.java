package com.nagpal.sahayak.service.model.Events;

import com.nagpal.sahayak.service.model.Entities.ExpenseCategoryResponse;

public class EventExpenseCategoryResponse {
    ExpenseCategoryResponse eventExpenseResponse;

    public EventExpenseCategoryResponse(ExpenseCategoryResponse info) {
        this.eventExpenseResponse = info;
    }

    public ExpenseCategoryResponse getLoginInfo() {
        return eventExpenseResponse;
    }
}
