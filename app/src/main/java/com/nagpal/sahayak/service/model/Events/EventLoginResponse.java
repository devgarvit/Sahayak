package com.nagpal.sahayak.service.model.Events;

import com.nagpal.sahayak.service.model.Entities.LoginResponse;

public class EventLoginResponse {
    LoginResponse loginInfo;

    public EventLoginResponse(LoginResponse info) {
        this.loginInfo = info;
    }

    public LoginResponse getLoginInfo() {
        return loginInfo;
    }
}
