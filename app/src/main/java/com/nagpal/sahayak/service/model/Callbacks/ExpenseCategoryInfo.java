package com.nagpal.sahayak.service.model.Callbacks;

import com.nagpal.sahayak.service.model.Entities.ExpenseCategoryResponse;
import com.nagpal.sahayak.service.model.Events.EventExpenseCategoryResponse;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExpenseCategoryInfo implements Callback<ExpenseCategoryResponse> {
    @Override
    public void onResponse(Call<ExpenseCategoryResponse> call, Response<ExpenseCategoryResponse> response) {
        EventBus.getDefault().post(new EventExpenseCategoryResponse(response.body()));
    }

    @Override
    public void onFailure(Call<ExpenseCategoryResponse> call, Throwable t) {
        EventBus.getDefault().post(new EventExpenseCategoryResponse(null));

    }
}
