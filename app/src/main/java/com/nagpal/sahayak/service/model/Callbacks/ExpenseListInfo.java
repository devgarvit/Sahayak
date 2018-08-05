package com.nagpal.sahayak.service.model.Callbacks;

import com.nagpal.sahayak.service.model.Entities.Expense;
import com.nagpal.sahayak.service.model.Entities.ExpenseList;
import com.nagpal.sahayak.service.model.Events.EventExpenseListResponse;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExpenseListInfo implements Callback<ExpenseList> {
    @Override
    public void onResponse(Call<ExpenseList> call, Response<ExpenseList> response) {
        EventBus.getDefault().post(new EventExpenseListResponse(response.body()));
    }

    @Override
    public void onFailure(Call<ExpenseList> call, Throwable t) {
        EventBus.getDefault().post(new EventExpenseListResponse(null));
    }
}
