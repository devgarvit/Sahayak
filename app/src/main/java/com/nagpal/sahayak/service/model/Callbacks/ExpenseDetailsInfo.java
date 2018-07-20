package com.nagpal.sahayak.service.model.Callbacks;

import com.nagpal.sahayak.service.model.Entities.ExpenseDetailsResponse;
import com.nagpal.sahayak.service.model.Events.EventExpenseDetailsResponse;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExpenseDetailsInfo implements Callback<ExpenseDetailsResponse> {
    @Override
    public void onResponse(Call<ExpenseDetailsResponse> call, Response<ExpenseDetailsResponse> response) {
        EventBus.getDefault().post(new EventExpenseDetailsResponse(response.body()));
    }

    @Override
    public void onFailure(Call<ExpenseDetailsResponse> call, Throwable t) {
        EventBus.getDefault().post(new EventExpenseDetailsResponse(null));
    }
}
