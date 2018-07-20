package com.nagpal.sahayak.view.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.nagpal.sahayak.R;
import com.nagpal.sahayak.service.model.Callbacks.LoginInfo;
import com.nagpal.sahayak.service.model.Events.EventLoginResponse;
import com.nagpal.sahayak.service.repository.ApiManager;
import com.nagpal.sahayak.utils.TinyDB;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    EditText editUserName, editPassword;
    AppCompatButton btnLogin;
    ProgressDialog asyncDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editUserName = (EditText) findViewById(R.id.edit_username);
        editPassword = (EditText) findViewById(R.id.edit_password);
        btnLogin = (AppCompatButton) findViewById(R.id.btn_login);
        asyncDialog = new ProgressDialog(this);
        btnLogin.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login: {
                validateInputs();
            }
        }
    }

    private void validateInputs() {
        String userName = editUserName.getText().toString();
        String password = editPassword.getText().toString();

        if (userName.trim().isEmpty()) {
            editUserName.setError("UserName can't be empty");
            return;
        }

        if (password.trim().isEmpty()) {
            editPassword.setError("Password can't be empty");
            return;
        }

        retrofitCall(userName, password);
    }

    private void retrofitCall(String userName, String password) {
        this.asyncDialog.setMessage("Please Wait...");
        this.asyncDialog.show();

        Map<String, String> params = new HashMap<String, String>();
        params.put("email", userName);
        params.put("password", password);
        new ApiManager().getAuthentication(params, new LoginInfo());

    }

    @Subscribe
    public void onEvent(EventLoginResponse event) {
        this.asyncDialog.dismiss();

        if (event != null && event.getLoginInfo() != null) {
            new TinyDB(getApplicationContext()).putString("access_token", event.getLoginInfo().getData().getUser().getAuthenticationToken());
            startActivity(new Intent(LoginActivity.this, HomePageActivity.class));
        } else {
            Toast.makeText(getApplicationContext(), "Something went wrong. Please reach us on sahayak1010@gmail.com", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }


}
