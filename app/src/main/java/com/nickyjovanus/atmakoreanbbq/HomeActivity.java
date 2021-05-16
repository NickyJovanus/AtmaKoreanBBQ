package com.nickyjovanus.atmakoreanbbq;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.button.MaterialButton;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

public class HomeActivity extends AppCompatActivity {

    CarouselView carouselView;
    int[] carouselImages = {R.drawable.carousel1, R.drawable.carousel2, R.drawable.carousel3};
    MaterialButton scanqr, reservasi;

    //Camera
    private static final int PERMISSION_CODE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        carouselView = findViewById(R.id.carouselView);
        carouselView.setPageCount(carouselImages.length);
        carouselView.setImageListener(imageListener);
    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setImageResource(carouselImages[position]);
        }
    };

    public void qrscan(View view){
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            //request enabling permission
            String[] permission = {Manifest.permission.CAMERA};

            //show popup to request permission
            requestPermissions(permission, PERMISSION_CODE);
        } else {
            startActivity(new Intent(HomeActivity.this, QRScanActivity.class));
        }
    }


    public void menu(View view) {
        startActivity(new Intent(HomeActivity.this, MenuActivity.class));
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Warning")
                .setMessage("Are you sure you want to close this app?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }
}