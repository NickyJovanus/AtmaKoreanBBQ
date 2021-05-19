package com.nickyjovanus.atmakoreanbbq;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.nickyjovanus.atmakoreanbbq.database.Customer;

public class CheckSplashActivity extends AppCompatActivity {

    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_splash);

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Customer customer = new Customer();
                Intent intent = new Intent(CheckSplashActivity.this, ReservationListActivity.class);
                startActivity(intent);
                finish();
            }
        },2000);
    }

}