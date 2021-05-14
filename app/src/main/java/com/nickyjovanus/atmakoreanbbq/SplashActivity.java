package com.nickyjovanus.atmakoreanbbq;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UserClass userClass = new UserClass();
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
        finish();
    }
}