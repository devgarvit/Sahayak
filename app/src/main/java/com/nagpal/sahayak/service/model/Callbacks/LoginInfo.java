package com.nagpal.sahayak.service.model.Callbacks;

import com.nagpal.sahayak.service.model.Entities.LoginResponse;
import com.nagpal.sahayak.service.model.Events.EventLoginResponse;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginInfo implements Callback<LoginResponse> {

    @Override
    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
        EventBus.getDefault().post(new EventLoginResponse(response.body()));
    }

    @Override
    public void onFailure(Call<LoginResponse> call, Throwable t) {
        EventBus.getDefault().post(new EventLoginResponse(null));
    }
}
