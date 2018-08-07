package com.nagpal.sahayak.view.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.nagpal.sahayak.R;
import com.nagpal.sahayak.utils.TinyDB;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                Intent intent;
                if (new TinyDB(getApplicationContext()).getString("access_token").equalsIgnoreCase("")) {
                    intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(SplashActivity.this, HomePageActivity.class);
                    startActivity(intent);
                }
                finish();
            }
        }, 2000);
    }
}
