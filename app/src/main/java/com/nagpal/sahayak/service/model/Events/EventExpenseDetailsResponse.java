package com.nagpal.sahayak.service.model.Events;

import com.nagpal.sahayak.service.model.Entities.ExpenseDetailsResponse;

public class EventExpenseDetailsResponse {

    ExpenseDetailsResponse response;

    public EventExpenseDetailsResponse(ExpenseDetailsResponse info) {
        this.response = info;
    }

    public ExpenseDetailsResponse getInfo() {
        return response;
    }
}
