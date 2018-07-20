package com.nagpal.sahayak.service.repository;

import com.nagpal.sahayak.service.model.Entities.ExpenseCategoryResponse;
import com.nagpal.sahayak.service.model.Entities.ExpenseDetailsResponse;
import com.nagpal.sahayak.service.model.Entities.ExpenseRequest;
import com.nagpal.sahayak.service.model.Entities.LoginResponse;

import java.util.Map;

import retrofit2.Callback;


public class ApiManager extends ApiClient {

    public void getAuthentication(Map<String, String> fieldMap, Callback<LoginResponse> loginInfoCallback) {
        getApiInterface().getAuthentication(fieldMap).enqueue(loginInfoCallback);
    }

    public void getExpenseCategories(String accessToken, Callback<ExpenseCategoryResponse> responseCallback) {
        getApiInterface().getExpenseCategories(accessToken).enqueue(responseCallback);
    }

    public void setExpenseDetails(String accessToken, ExpenseRequest expenseRequest, Callback<ExpenseDetailsResponse> responseCallback) {
        getApiInterface().setExpenseDetails(accessToken, expenseRequest).enqueue(responseCallback);
    }
}
