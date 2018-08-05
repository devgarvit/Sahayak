package com.nagpal.sahayak.service.repository;

import com.nagpal.sahayak.service.model.Entities.ExpenseCategoryResponse;
import com.nagpal.sahayak.service.model.Entities.ExpenseDetailsResponse;
import com.nagpal.sahayak.service.model.Entities.ExpenseList;
import com.nagpal.sahayak.service.model.Entities.ExpenseRequest;
import com.nagpal.sahayak.service.model.Entities.LoginResponse;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;


public interface ApiInterface<M> {

    @POST("/v1/sessions")
    Call<LoginResponse> getAuthentication(@QueryMap(encoded = true) Map<String, String> params);

    @GET("/v1/categories/list")
    Call<ExpenseCategoryResponse> getExpenseCategories(@Header("token") String params);

    @POST("/v1/expenses")
    Call<ExpenseDetailsResponse> setExpenseDetails(@Header("token") String accessToken, @Body ExpenseRequest expenseRequest);

    @GET("/v1/expenses")
    Call<ExpenseList> setExpenseDetails(@Header("token") String accessToken);
}
